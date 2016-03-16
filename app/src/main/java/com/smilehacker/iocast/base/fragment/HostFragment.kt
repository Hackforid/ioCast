package com.smilehacker.iocast.base.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smilehacker.iocast.base.BaseFragment
import com.smilehacker.iocast.base.activity.HostActivity
import com.smilehacker.iocast.util.DLog

/**
 * Created by kleist on 16/3/9.
 */
abstract class HostFragment : BaseFragment() {

    val NAME : String by lazy { javaClass.simpleName }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        DLog.d(javaClass.simpleName + " onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DLog.d(javaClass.simpleName + " onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        DLog.d(javaClass.simpleName + " onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        DLog.d(javaClass.simpleName + " onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        DLog.d(javaClass.simpleName + " onStart")
    }

    override fun onPause() {
        super.onPause()
        DLog.d(javaClass.simpleName + " onPause")
    }

    override fun onStop() {
        super.onStop()
        DLog.d(javaClass.simpleName + " onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        DLog.d(javaClass.simpleName + " onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        DLog.d(javaClass.simpleName + " onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        DLog.d(javaClass.simpleName + " onDetach")
    }

    fun getHostActivity() : HostActivity? {
        if (activity != null && activity is HostActivity) {
            return activity as HostActivity
        } else {
            return null
        }
    }

    inline fun <reified T : HostFragment> startFragment(bundle : Bundle? = null) : T? {
        val frg = getHostActivity()?.startFragment<T>(bundle)
        return frg
    }

    fun finish() {
        getHostActivity()?.finishFragment()
    }
}