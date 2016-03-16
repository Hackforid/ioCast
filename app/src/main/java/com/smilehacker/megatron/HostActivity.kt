package com.smilehacker.megatron

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.smilehacker.megatron.anim.DefaultVerticalAnimator
import com.smilehacker.megatron.anim.FragmentAnimator

/**
 * Created by kleist on 16/3/3.
 */
abstract class HostActivity : AppCompatActivity() {

    private val mFragmentation = Fragmentation(this, getContainerID())

    private lateinit var mFragmentAnimator : FragmentAnimator
    private var mPopMultipleNoAnim = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onHandleSaveInstanceState(savedInstanceState)
        mFragmentAnimator = onCreateFragmentAnimator()
    }

    abstract fun getContainerID() : Int

    protected open fun onHandleSaveInstanceState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val fragments = supportFragmentManager.fragments

            if (fragments != null && fragments.size > 0) {
                var showFlag = false

                val ft = supportFragmentManager.beginTransaction()
                for (i in fragments.indices.reversed()) {
                    val fragment = fragments[i]
                    if (fragment != null) {
                        if (!showFlag) {
                            ft.show(fragments[i])
                            showFlag = true
                        } else {
                            ft.hide(fragments[i])
                        }
                    }
                }
                ft.commit()
            }
        }
    }

    val fragmentation : Fragmentation
        get() = mFragmentation

    override fun onBackPressed() {
        val topFrg = getTopFragment()
        if (topFrg != null) {
            if (topFrg.onBackPressed()) {
                return
            }
        }

        if (supportFragmentManager.backStackEntryCount > 1) {
            mFragmentation.back(supportFragmentManager)
        } else {
            finish()
        }
    }

    fun getTopFragment() : KitFragment? {
        return Fragmentation.getTopFragment(supportFragmentManager)
    }

    fun <T : KitFragment> findFragment(fragmentClass : Class<T>) : T? {
        return mFragmentation.findStackFragment(fragmentClass, supportFragmentManager)
    }

    fun popFragment() {
        mFragmentation.back(supportFragmentManager)
    }

    fun popToFragment(fragmentClass: Class<out Fragment>, includeSelf: Boolean = false, afterPopTransactionRunnable: Runnable? = null) {
        mFragmentation.popTo(fragmentClass, includeSelf, afterPopTransactionRunnable, supportFragmentManager)
    }

    fun startFragment(to : KitFragment, launchMode : Int = KitFragment.STANDARD) {
        mFragmentation.dispatchTransaction(getTopFragment(), to, 0, launchMode, Fragmentation.TYPE_ADD)
    }

    fun startFragmentForResult(to: KitFragment, requestCode : Int = 0) {
        mFragmentation.dispatchTransaction(getTopFragment(), to, requestCode, KitFragment.STANDARD, Fragmentation.TYPE_ADD)
    }

    fun startFragmentWithFinish(to : KitFragment) {
        mFragmentation.dispatchTransaction(getTopFragment(), to, 0, KitFragment.STANDARD, Fragmentation.TYPE_ADD_FINISH)
    }

    internal fun preparePopMultiple() {
        mPopMultipleNoAnim = true
    }

    internal fun finishPopMultiple() {
        mPopMultipleNoAnim = false
    }

    fun getFragmentAnimator(): FragmentAnimator {
        return FragmentAnimator(
                mFragmentAnimator.enter, mFragmentAnimator.exit,
                mFragmentAnimator.popEnter, mFragmentAnimator.popExit)
    }

    fun setFragmentAnimator(fragmentAnimator: FragmentAnimator) {
        this.mFragmentAnimator = fragmentAnimator
    }

    /**
     * 创建全局Fragment的切换动画

     * @return
     */
    protected open fun onCreateFragmentAnimator(): FragmentAnimator {
        return DefaultVerticalAnimator()
    }


    inline fun <reified T : Fragment> TAG() : String {
        return T::class.java.simpleName
    }

    fun getPopMultipleNoAnim() : Boolean {
        return mPopMultipleNoAnim
    }

}


