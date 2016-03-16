package com.smilehacker.iocast.base.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransactionBugFixHack
import com.smilehacker.iocast.R
import com.smilehacker.iocast.base.fragment.HostFragment
import com.smilehacker.iocast.base.newBaseFragment
import com.smilehacker.iocast.rsslist.RssListFragment
import java.util.*

/**
 * Created by kleist on 16/3/3.
 */
class HostActivity : BaseActivity() {

    private val mFragmentKeys = ArrayList<String>()

    private val CONTATINER = R.id.host_container

    protected var mCurrentFragment : HostFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_host)
        FragmentTransactionBugFixHack.reorderIndices(supportFragmentManager)
        init()
    }

    fun init() {
        startFragment<RssListFragment>()
    }

    inline fun <reified T : HostFragment> startFragment(bundle : Bundle? = null) : T {
        val ft = supportFragmentManager.beginTransaction()
        val frg = newBaseFragment<T>(bundle)
        val tag = frg.NAME
        ft.add(R.id.host_container, frg, tag)
        if (mCurrentFragment != null) {
            ft.hide(mCurrentFragment)
        }
        ft.show(frg)

        ft.addToBackStack(tag)

        ft.commit()

        mCurrentFragment = frg

        return frg
    }

    fun finishFragment() {
//        val ft = supportFragmentManager.beginTransaction()
//        val frgs = supportFragmentManager.fragments
//        frgs.forEach { DLog.d((it as HostFragment<*, *>).NAME) }
//        if (frgs.size <= 1) {
//            finish()
//        } else {
//            ft.remove(frgs[frgs.size - 1])
//            ft.show(frgs[frgs.size -2])
//        }
//        ft.commit()
    }

    inline fun <reified T> openFragment(fragmentClass: Class<T> , bundle : Bundle? = null) : T {
        if (T::class !is HostFragment) {
            throw IllegalArgumentException("must be HostFragment")
        }

        val frgs = supportFragmentManager.fragments;
        val frgTag = fragmentClass.simpleName

        // show target
        val ft = supportFragmentManager.beginTransaction()
        var fragment = supportFragmentManager.findFragmentByTag(frgTag);
        if (fragment == null) {
            fragment = newBaseFragment(bundle)
        }
        ft.show(fragment)

        // hide others
        if (frgs != null && frgs.size > 0) {
            frgs.forEach {
                if (it != null) {
                    if (!frgTag.equals(it.tag)) {
                        ft.hide(it)
                    }
                }
            }
        }

        ft.commit()

        return fragment as T
    }

    inline fun <reified T : Fragment> TAG() : String {
        return T::class.java.simpleName
    }


}


