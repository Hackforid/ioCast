package com.smilehacker.megatron

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import com.smilehacker.iocast.R
import com.smilehacker.megatron.anim.FragmentAnimator

/**
 * Created by kleist on 16/3/15.
 */
abstract class KitFragment : Fragment() {

    companion object {
        // LaunchMode
        val STANDARD = 0
        val SINGLETOP = 1
        val SINGLETASK = 2

        val RESULT_CANCELED = 0
        val RESULT_OK = 1

        val STATE_SAVE_ENTER = "state_save_enter"
        val STATE_SAVE_EXIT = "state_save_exit"
        val STATE_SAVE_POP_ENTER = "state_save_pop_enter"
        val STATE_SAVE_POP_EXIT = "state_save_pop_exit"

    }

    private var mRequestCode: Int = 0
    private var mResultCode: Int = RESULT_CANCELED
    private var mResultBundle: Bundle? = null
    private var mIsRoot = false

    private var mNewBundle: Bundle? = null

    private var mOnAnimEndListener: OnAnimEndListener? = null
    private var mNeedAnimListener = false

    private lateinit var mFragmentation: Fragmentation

    private lateinit var mActivity: HostActivity

    private var mEnter: Int = 0
    private var mExit: Int = 0
    private var mPopEnter: Int = 0
    private var mPopExit: Int = 0
    private var mNoAnim: Animation? = null
    private var mEnterAnim: Animation? = null
    private var mExitAnim: Animation? = null
    private var mPopEnterAnim: Animation? = null
    private var mPopExitAnim: Animation? = null

    private var mBgColor: Int = 0
    private var mNeedHideSoft: Boolean = false  // 隐藏软键盘

    private val mIMM: InputMethodManager by lazy { mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is HostActivity) {
            mActivity = context
        } else {
            throw IllegalStateException(context.toString() + " should extend HostActivity")
        }
    }

    override final fun onAttach(activity: Activity?) {
        super.onAttach(activity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = arguments
        if (bundle != null) {
            mRequestCode = bundle.getInt(Fragmentation.KEY_REQUEST_CODE, 0)
            mResultCode = bundle.getInt(Fragmentation.KEY_RESULT_CODE, RESULT_CANCELED)
            mResultBundle = bundle.getBundle(Fragmentation.KEY_RESULT_BUNDLE)
            mIsRoot = bundle.getBoolean(Fragmentation.KEY_IS_ROOT, false)
        }

        if (savedInstanceState == null) {
            var fragmentAnimator = onCreateFragmentAnimation()
            if (fragmentAnimator == null) {
                fragmentAnimator = mActivity.getFragmentAnimator()
            }

            mEnter = fragmentAnimator.enter
            mExit = fragmentAnimator.exit
            mPopEnter = fragmentAnimator.popEnter
            mPopExit = fragmentAnimator.popExit
        } else {
            mEnter = savedInstanceState.getInt(STATE_SAVE_ENTER)
            mExit = savedInstanceState.getInt(STATE_SAVE_EXIT)
            mPopEnter = savedInstanceState.getInt(STATE_SAVE_POP_ENTER)
            mPopExit = savedInstanceState.getInt(STATE_SAVE_POP_EXIT)
        }

        handleNoAnim()

        mNoAnim = AnimationUtils.loadAnimation(mActivity, R.anim.no_anim)
        mEnterAnim = AnimationUtils.loadAnimation(mActivity, mEnter)
        mExitAnim = AnimationUtils.loadAnimation(mActivity, mExit)
        mPopEnterAnim = AnimationUtils.loadAnimation(mActivity, mPopEnter)
        mPopExitAnim = AnimationUtils.loadAnimation(mActivity, mPopExit)
    }

    private fun handleNoAnim() {
        if (mEnter == 0) {
            mEnter = R.anim.no_anim
        }
        if (mExit == 0) {
            mExit = R.anim.no_anim
        }
        if (mPopEnter == 0) {
            mPopEnter = R.anim.no_anim
        }
        if (mPopExit == 0) {
            mPopExit = R.anim.pop_exit_no_anim
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState!!.putInt(STATE_SAVE_ENTER, mEnter)
        outState.putInt(STATE_SAVE_EXIT, mExit)
        outState.putInt(STATE_SAVE_POP_ENTER, mPopEnter)
        outState.putInt(STATE_SAVE_POP_EXIT, mPopExit)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val view = view
        if (view != null && view.background == null) {
            if (mBgColor != 0) {
                view.setBackgroundColor(mBgColor)
            } else {
                view.setBackgroundColor(Color.WHITE)
            }
            view.isClickable = true
        }

        mFragmentation = mActivity.fragmentation
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onShow()
    }

    open fun onBackPressed(): Boolean {
        val top = getTopChildFragment()
        if (top != null) {
            return top.onBackPressed()
        }
        return false
    }

    /**
     * 设定当前Fragmemt动画,优先级比在SupportActivity里高
     */
    protected open fun onCreateFragmentAnimation(): FragmentAnimator? {
        return null
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        if (mActivity.getPopMultipleNoAnim()) {
            return mNoAnim
        }
        if (transit == FragmentTransaction.TRANSIT_FRAGMENT_OPEN) {
            if (enter) {
                if (mIsRoot) {
                    return mNoAnim
                }
                if (mNeedAnimListener) {
                    mEnterAnim?.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation) {

                        }

                        override fun onAnimationEnd(animation: Animation) {
                            mEnterAnim?.setAnimationListener(null)
                            mNeedAnimListener = false
                            mOnAnimEndListener?.onAnimationEnd()
                        }

                        override fun onAnimationRepeat(animation: Animation) {

                        }
                    })
                }
                return mEnterAnim
            } else {
                return mPopExitAnim
            }
        } else if (transit == FragmentTransaction.TRANSIT_FRAGMENT_CLOSE) {
            if (enter) {
                return mPopEnterAnim
            } else {
                return mExitAnim
            }
        }
        return null
    }


    fun getRequestCode(): Int {
        return mRequestCode
    }

    fun getResultCode(): Int {
        return mResultCode
    }

    fun getResultBundle(): Bundle? {
        return mResultBundle
    }

    open fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {

    }

    open fun onNewBundle(args: Bundle?) {
    }

    /**
     * 添加NewBundle,用于启动模式为SingleTask/SingleTop时
     */
    internal fun putNewBundle(newBundle: Bundle) {
        this.mNewBundle = newBundle
    }

    internal fun getNewBundle(): Bundle? {
        return mNewBundle
    }

    internal fun setNeedAnimListener(needAnimListener: Boolean, onAnimEndListener: OnAnimEndListener?) {
        this.mNeedAnimListener = needAnimListener
        this.mOnAnimEndListener = onAnimEndListener
    }


    protected fun setFragmentBackground(color: Int) {
        mBgColor = color
    }

    protected fun hideSoftInput() {
        if (view != null) {
            mIMM.hideSoftInputFromWindow(view!!.windowToken, 0)
        }
    }

    protected fun showSoftInput(view: View?) {
        if (view == null) return
        view.requestFocus()
        mNeedHideSoft = true
        view.postDelayed(Runnable { mIMM.showSoftInput(view, InputMethodManager.SHOW_FORCED) }, 200)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            onHidden()
        } else {
            onShow()
        }
    }

    override fun onPause() {
        super.onPause()
        if (mNeedHideSoft) {
            hideSoftInput()
        }
    }

    protected open fun onShow() {
    }

    protected open fun onHidden() {
    }

    fun popToFragment(fragmentClass: Class<out KitFragment>, includeSelf: Boolean = false, afterPopTransactionRunnable: Runnable? = null) {
        mFragmentation.popTo(fragmentClass, includeSelf, afterPopTransactionRunnable, fragmentManager)
    }

    fun popFragment() {
        mFragmentation.back(fragmentManager)
    }

    fun startFragment(toFragment: KitFragment, launchMode: Int = STANDARD) {
        mFragmentation.dispatchTransaction(this, toFragment, 0, launchMode, Fragmentation.TYPE_ADD)
    }

    fun startFragmentForResult(to: KitFragment, requestCode: Int) {
        mFragmentation.dispatchTransaction(this, to, requestCode, STANDARD, Fragmentation.TYPE_ADD)
    }

    fun starFragmenttWithFinish(to: KitFragment) {
        mFragmentation.dispatchTransaction(this, to, 0, STANDARD, Fragmentation.TYPE_ADD_FINISH)
    }

    /**
     * 获取栈内的子framgent对象

     * @param fragmentClass
     */
    @SuppressWarnings("unchecked")
    fun <T : Fragment> findChildFragment(fragmentClass: Class<T>): T? {
        val fragment = childFragmentManager.findFragmentByTag(fragmentClass.name) ?: return null
        return fragment as T?
    }

    /**
     * 替换子Fragment

     * @param childContainer
     * *
     * @param childFragment
     * *
     * @param addToBack
     */
    fun replaceChildFragment(childContainer: Int, childFragment: Fragment, addToBack: Boolean) {
        val ft = childFragmentManager.beginTransaction()
        ft.replace(childContainer, childFragment, childFragment.javaClass.name).show(childFragment)
        if (addToBack) {
            ft.addToBackStack(childFragment.javaClass.name)
        }
        ft.commit()
    }


    fun startChildFragment(childContainer: Int, childFragment: Fragment, addToBack: Boolean) {
        val ft = childFragmentManager.beginTransaction()
        ft.add(childContainer, childFragment, childFragment.javaClass.name).show(childFragment)
        if (addToBack) {
            ft.addToBackStack(childFragment.javaClass.name)
        }
        ft.commit()
    }

    /**
     * 替换 兄弟Fragment

     * @param brotherContainer
     * *
     * @param brotherFragment
     * *
     * @param addToBack
     */
    fun replaceBrotherFragment(brotherContainer: Int, brotherFragment: Fragment, addToBack: Boolean) {
        val ft = fragmentManager.beginTransaction()
        ft.replace(brotherContainer, brotherFragment, brotherFragment.javaClass.name).show(brotherFragment)
        if (addToBack) {
            ft.addToBackStack(brotherFragment.javaClass.name)
        }
        ft.commit()
    }

    /**
     * 添加 兄弟Fragment

     * @param brotherContainer
     * *
     * @param brotherFragment
     * *
     * @param addToBack
     */
    fun startBrotherFragment(brotherContainer: Int, brotherFragment: Fragment, addToBack: Boolean) {
        val ft = fragmentManager.beginTransaction()
        ft.add(brotherContainer, brotherFragment, brotherFragment.javaClass.name).show(brotherFragment)
        if (addToBack) {
            ft.addToBackStack(brotherFragment.javaClass.name)
        }
        ft.commit()
    }


    fun getTopChildFragment(): KitFragment? {
        return Fragmentation.getTopFragment(childFragmentManager)
    }

    fun getTopFragment(): KitFragment? {
        return Fragmentation.getTopFragment(fragmentManager)
    }
}