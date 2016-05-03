package com.smilehacker.iocast.ui.index

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smilehacker.iocast.base.mvp.MVPFragment

/**
 * Created by kleist on 16/4/24.
 */

class IndexFragment : MVPFragment<IndexPresenter, IndexViewer>(), IndexViewer {

    override fun createPresenter(): IndexPresenter {
        return IndexPresenterImpl()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}
