package com.smilehacker.iocast.base.mvp

import java.lang.ref.WeakReference

/**
 * Created by kleist on 15/12/2.
 */
abstract class BasePresenter<V : Viewer> : Presenter<V> {
    private var mViewRef : WeakReference<V>? = null

    public var view : V? = null
        get() = mViewRef?.get()
        private set

    override fun attachView(view: V) {
        mViewRef = WeakReference<V>(view)
    }

    override fun detachView(retainInstance: Boolean) {
        mViewRef?.clear()
        mViewRef = null
    }

    public fun isViewAttached() : Boolean {
        return mViewRef?.get() != null
    }

    open fun onShow() {

    }

    open fun onHidden() {

    }
}