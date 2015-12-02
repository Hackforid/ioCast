package com.smilehacker.iocast.base.mvp

/**
 * Created by kleist on 15/12/2.
 */
interface Presenter<V : Viewer> {

    //var mView : Viewer?

    public fun attachView(view : V)

    public fun detachView(retainInstance : Boolean)

}