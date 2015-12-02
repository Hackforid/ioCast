package com.smilehacker.iocast.base.mvp

import android.os.Bundle
import com.smilehacker.iocast.base.BaseActivity

/**
 * Created by kleist on 15/12/2.
 */

abstract class MVPActivity<P : Presenter<V>, V : Viewer>: BaseActivity() {

    private val mPresenter : P by lazy { createPresenter() }
    public val presenter = mPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (this !is Viewer) {
            throw NotImplementedError("Viewer not implemented")
        }
        presenter.attachView(this as V)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView(false)
    }

    protected abstract fun createPresenter() : P
}

