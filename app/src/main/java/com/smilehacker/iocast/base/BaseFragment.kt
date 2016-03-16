package com.smilehacker.iocast.base

import android.os.Bundle
import android.support.v4.app.Fragment

/**
 * Created by kleist on 15/11/30.
 */
abstract class BaseFragment : Fragment() {

    companion object {
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

inline fun <reified T : BaseFragment> newBaseFragment(bundle : Bundle? = null) : T {
    val frg = T::class.java.constructors[0].newInstance() as T
    frg.arguments = bundle
    return frg
}

inline fun <reified T : BaseFragment> BaseFragment.Companion.TAG() : String {
    return T::class.java.simpleName
}

