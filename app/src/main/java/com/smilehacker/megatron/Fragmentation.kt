package com.smilehacker.megatron

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.app.FragmentTransactionBugFixHack
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast

/**
 * Created by kleist on 16/3/15.
 */
class Fragmentation(val mActivity: HostActivity, val mContainerId: Int) {



    private var mFragmentManager = mActivity.supportFragmentManager
    private val mHandler = Handler()

    private var mCurrentTime: Long = 0


    companion object {

        val TYPE_ADD = 0
        val TYPE_ADD_FINISH = 1

        val CLICK_SPACE_TIME = 400
        val KEY_REQUEST_CODE = "key_request_code"
        val KEY_RESULT_CODE = "key_result_code"
        val KEY_RESULT_BUNDLE = "key_bundle"
        val KEY_IS_ROOT = "key_is_root"

        fun getTopFragment(fragmentManager: FragmentManager): KitFragment? {
            val fragments = fragmentManager.fragments
            if (fragments != null) {
                for (i in (fragments.size - 1) downTo 0) {
                    val fragment = fragments[i]
                    if (fragment != null && fragment is KitFragment) {
                        return fragment
                    }
                }
            }
            return null
        }
    }

    internal fun dispatchTransaction(from: KitFragment?, to: KitFragment, requestCode: Int,
                                     launchMode: Int, type: Int) {
        if (System.currentTimeMillis() - mCurrentTime < CLICK_SPACE_TIME) {
            return
        }
        mCurrentTime = System.currentTimeMillis()

        if (from != null) {
            mFragmentManager = from.fragmentManager
        }

        FragmentTransactionBugFixHack.reorderIndices(mFragmentManager)

        if (type == TYPE_ADD) {
            saveRequestCode(to, requestCode)
        }

        if (handleLaunchMode(to, launchMode)) {
            return
        }

        when (type) {
            TYPE_ADD -> start(from, to)
            TYPE_ADD_FINISH -> if (from != null) {
                startWithFinish(from, to)
            } else {
                throw RuntimeException("startWithFinish(): getTopFragment() is null")
            }
        }
    }

    internal fun start(from: KitFragment?, to: KitFragment) {
        val toName = to.javaClass.name
        val ft = mFragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).add(mContainerId, to, toName).show(to)

        if (from != null) {
            ft.hide(from)
        } else {
            val bundle = to.arguments
            bundle.putBoolean(KEY_IS_ROOT, true)
        }

        ft.addToBackStack(toName)
        ft.commit()
    }

    internal fun startWithFinish(from: KitFragment, to: KitFragment) {
        val preFragment = handlerFinish(from, to)
        passSaveResult(from, to)

        mFragmentManager.beginTransaction().remove(from).commit()
        mFragmentManager.popBackStack()

        val toName = to.javaClass.name
        val ft = mFragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).add(mContainerId, to, toName).show(to).addToBackStack(toName)

        if (preFragment != null) {
            ft.hide(preFragment)
        }
        ft.commit()
    }

    /**
     * pass on Result

     * @param from
     * *
     * @param to
     */
    private fun passSaveResult(from: KitFragment, to: KitFragment) {
        saveRequestCode(to, from.getRequestCode())
        val bundle = to.arguments
        bundle.putInt(KEY_RESULT_CODE, from.getResultCode())
        bundle.putBundle(KEY_RESULT_BUNDLE, from.getResultBundle())
    }

    fun back(fragmentManager: FragmentManager) {
        if (fragmentManager.backStackEntryCount > 1) {
            handleBack(fragmentManager)
        }
    }

    private fun handleBack(fragmentManager: FragmentManager) {
        val fragments = fragmentManager.fragments
        var findTop = false

        var resultCode = 0
        var requestCode = 0
        var data: Bundle? = null

        if (fragments != null) {
            for (i in (fragments.size) - 1 downTo 0) {
                val fragment = fragments[i]
                if (fragment != null && fragment is KitFragment) {
                    val kitFrg = fragment
                    if (!findTop) {
                        resultCode = kitFrg.getResultCode()
                        requestCode = kitFrg.getRequestCode()
                        data = kitFrg.getResultBundle()
                        findTop = true
                    } else {
                        if (requestCode != 0) {
                            kitFrg.onFragmentResult(requestCode, resultCode, data)
                        }

                        // 解决在app因资源问题被回收后 重新进入app 在Fragment嵌套时,返回到嵌套的Fragment时,导致的错误问题
                        if (kitFrg.childFragmentManager.fragments != null) {
                            fragmentManager.beginTransaction().show(kitFrg).commit()
                        }

                        break
                    }

                }
            }
        }

        fragmentManager.popBackStack()
    }

    /**
     * fix anim
     */
    private fun handlerFinish(from: KitFragment, to: KitFragment): Fragment? {
        val preFragment = getPreFragment(from)
        if (preFragment != null) {
            val view = preFragment.view
            if (view != null) {
                // 不调用 会闪屏
                view.visibility = View.VISIBLE

                val viewGroup: ViewGroup
                val fromView = from.view

                if (fromView != null) {
                    if (view is ViewGroup) {
                        viewGroup = view
                        val container = mActivity.findViewById(mContainerId) as ViewGroup?
                        if (container != null) {
                            container.removeView(fromView)
                            if (fromView.layoutParams.height != ViewGroup.LayoutParams.MATCH_PARENT) {
                                fromView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                            }

                            if (viewGroup is LinearLayout) {
                                viewGroup.addView(fromView, 0)
                            } else {
                                viewGroup.addView(fromView)
                            }

                            val finalViewGroup = viewGroup
                            to.setNeedAnimListener(true, object : OnAnimEndListener {
                                override fun onAnimationEnd() {
                                    finalViewGroup.removeView(fromView)
                                }
                            })
                        }
                    }
                }
            }
        }
        return preFragment
    }

    fun <T : KitFragment> findStackFragment(fragmentClass: Class<T>, fragmentManager: FragmentManager): T? {
        val fragment = fragmentManager.findFragmentByTag(fragmentClass.name) ?: return null
        return fragment as T?
    }

    /**
     * 出栈到目标fragment

     * @param fragmentClass 目标fragment
     * *
     * @param includeSelf   是否包含该fragment
     */
    internal fun popTo(fragmentClass: Class<out Fragment>, includeSelf: Boolean, afterPopTransactionRunnable: Runnable?, fragmentManager: FragmentManager) {
        // TODO 没看懂 没遇到过
        try {
            var rootFragment = fragmentManager.findFragmentByTag(fragmentClass.name)
            if (includeSelf) {
                rootFragment = getPreFragment(rootFragment)
            }
            val fromFragment = getTopFragment(fragmentManager)

            if (rootFragment === fromFragment && afterPopTransactionRunnable != null) {
                mHandler.post(afterPopTransactionRunnable)
                return
            }

            fixPopToAnim(rootFragment, fromFragment)
            fragmentManager.beginTransaction().remove(fromFragment).commit()

            val flag = if (includeSelf) FragmentManager.POP_BACK_STACK_INCLUSIVE else 0
            popBackFix(fragmentClass, flag, fragmentManager)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(mActivity, "Exception", Toast.LENGTH_SHORT).show()
        }

        if (afterPopTransactionRunnable != null) {
            mHandler.post(afterPopTransactionRunnable)
        }
    }

    private fun getPreFragment(fragment: Fragment): Fragment? {
        val fragmentList = mFragmentManager.fragments
        var preFragment: Fragment? = null
        if (fragmentList != null) {
            val index = fragmentList.indexOf(fragment)
            for (i in (index - 1) downTo 0) {
                preFragment = fragmentList[i]
                if (preFragment != null) {
                    break
                }
            }
        }
        return preFragment
    }

    /**
     * 解决pop多个fragment异常问题

     * @param fragmentClass
     * *
     * @param flag
     */
    private fun popBackFix(fragmentClass: Class<*>, flag: Int, fragmentManager: FragmentManager) {
        mActivity.preparePopMultiple()
        fragmentManager.popBackStackImmediate(fragmentClass.name, flag)
        mActivity.finishPopMultiple()
    }

    /**
     * fix popTp anim
     */
    private fun fixPopToAnim(rootFragment: Fragment?, fromFragment: KitFragment?) {
        if (rootFragment != null) {
            val view = rootFragment.view
            if (view != null) {
                // 不调用 会闪屏
                view.visibility = View.VISIBLE

                val viewGroup: ViewGroup
                val fromView = fromFragment?.view

                if (fromView != null) {
                    if (view is ViewGroup) {
                        viewGroup = view
                        val container = mActivity.findViewById(mContainerId) as ViewGroup?
                        if (container != null) {
                            container.removeView(fromView)
                            if (fromView.layoutParams.height != ViewGroup.LayoutParams.MATCH_PARENT) {
                                fromView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                            }

                            if (viewGroup is LinearLayout) {
                                viewGroup.addView(fromView, 0)
                            } else {
                                viewGroup.addView(fromView)
                            }

                            val finalViewGroup = viewGroup
                            mHandler.postDelayed({ finalViewGroup.removeView(fromView) }, 200)
                        }
                    }
                }
            }
        }
    }

    private fun saveRequestCode(to: Fragment, requestCode: Int) {
        var bundle: Bundle? = to.arguments
        if (bundle == null) {
            bundle = Bundle()
            to.arguments = bundle
        }
        bundle.putInt(KEY_REQUEST_CODE, requestCode)
    }

    /**
     * handle LaunchMode

     * @param to
     * *
     * @param launchMode
     * *
     * @return
     */
    private fun handleLaunchMode(to: Fragment, launchMode: Int): Boolean {
        val fragment = mFragmentManager.findFragmentByTag(to.javaClass.name)

        if (fragment != null) {
            if (launchMode == KitFragment.SINGLETOP) {
                val fragments = mFragmentManager.fragments
                val index = fragments.indexOf(fragment)
                // 在栈顶
                if (index == mFragmentManager.backStackEntryCount - 1) {
                    if (handleNewBundle(to, fragment)) return true
                }
            } else if (launchMode == KitFragment.SINGLETASK) {
                popBackFix(to.javaClass, 0, mFragmentManager)
                if (handleNewBundle(to, fragment)) return true
            }
        }
        return false
    }

    private fun handleNewBundle(to: Fragment, fragment: Fragment): Boolean {
        if (fragment is KitFragment) {
            val supportTo = to as KitFragment
            val newBundle = supportTo.getNewBundle()
            supportTo.onNewBundle(newBundle)
            return true
        }
        return false
    }
}