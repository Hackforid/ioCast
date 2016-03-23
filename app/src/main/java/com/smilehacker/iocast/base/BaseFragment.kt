package com.smilehacker.iocast.base

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smilehacker.megatron.KitFragment
import java.io.Serializable

/**
 * Created by kleist on 15/11/30.
 */
abstract class BaseFragment : KitFragment() {

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        //DLog.d(javaClass.simpleName + " onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //DLog.d(javaClass.simpleName + " onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //DLog.d(javaClass.simpleName + " onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //DLog.d(javaClass.simpleName + " onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        //DLog.d(javaClass.simpleName + " onStart")
    }

    override fun onPause() {
        super.onPause()
        //DLog.d(javaClass.simpleName + " onPause")
    }

    override fun onStop() {
        super.onStop()
        //DLog.d(javaClass.simpleName + " onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //DLog.d(javaClass.simpleName + " onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        //DLog.d(javaClass.simpleName + " onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        //DLog.d(javaClass.simpleName + " onDetach")
    }
}

inline fun <reified T : KitFragment> newFragment(bundle : Bundle? = null) : T {
    val fragment = T::class.java.constructors[0].newInstance() as T
    fragment.arguments = bundle
    return fragment
}

inline fun <reified T : KitFragment> newFragment(vararg params: Pair<String, Any>) : T {
    val fragment = T::class.java.constructors[0].newInstance() as T

    if (params.isNotEmpty()) {
        val bundle = Bundle()
        fillIntentArguments(bundle, params)
        fragment.arguments = bundle
    }
    return fragment
}

fun fillIntentArguments(bundle: Bundle, params: Array<out Pair<String, Any>>) {
    params.forEach {
        val value = it.second
        when (value) {
            is Int -> bundle.putInt(it.first, value)
            is Long -> bundle.putLong(it.first, value)
            is CharSequence -> bundle.putCharSequence(it.first, value)
            is String -> bundle.putString(it.first, value)
            is Float -> bundle.putFloat(it.first, value)
            is Double -> bundle.putDouble(it.first, value)
            is Char -> bundle.putChar(it.first, value)
            is Short -> bundle.putShort(it.first, value)
            is Boolean -> bundle.putBoolean(it.first, value)
            is Serializable -> bundle.putSerializable(it.first, value)
            is Bundle -> bundle.putBundle(it.first, value)
            is Parcelable -> bundle.putParcelable(it.first, value)
            is Array<*> -> when {
                value.isArrayOf<CharSequence>() -> bundle.putCharSequenceArray(it.first, value as Array<out CharSequence>?)
                value.isArrayOf<String>() -> bundle.putStringArray(it.first, value as Array<out String>?)
                value.isArrayOf<Parcelable>() -> bundle.putParcelableArray(it.first, value as Array<out Parcelable>?)
                else -> throw Exception("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
            }
            is IntArray -> bundle.putIntArray(it.first, value)
            is LongArray -> bundle.putLongArray(it.first, value)
            is FloatArray -> bundle.putFloatArray(it.first, value)
            is DoubleArray -> bundle.putDoubleArray(it.first, value)
            is CharArray -> bundle.putCharArray(it.first, value)
            is ShortArray -> bundle.putShortArray(it.first, value)
            is BooleanArray -> bundle.putBooleanArray(it.first, value)
            else -> throw Exception("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
        }
    }
}