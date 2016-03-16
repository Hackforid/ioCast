package com.smilehacker.iocast.base.mvp

import android.os.Bundle
import android.view.View
import com.smilehacker.iocast.base.fragment.HostFragment

/**
 * Created by kleist on 15/12/2.
 */
abstract class MVPFragment<P : Presenter<V>, V : Viewer> : HostFragment() {
    private val mPresenter : P by lazy { createPresenter() }
    val presenter = mPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (this !is Viewer) {
            throw NotImplementedError("Viewer not implemented")
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.attachView(this as V)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter.detachView(retainInstance)
    }

    protected abstract fun createPresenter() : P
}


