package com.smilehacker.iocast.rsslist

import com.smilehacker.iocast.base.mvp.Presenter

/**
 * Created by kleist on 15/12/3.
 */
interface IRssListPresenter : Presenter<RssListViewer> {
    fun loadData()
}
