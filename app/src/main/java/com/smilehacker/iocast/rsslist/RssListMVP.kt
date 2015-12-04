package com.smilehacker.iocast.rsslist

import com.smilehacker.iocast.base.mvp.BasePresenter
import com.smilehacker.iocast.base.mvp.Viewer
import com.smilehacker.iocast.model.PodcastRSS

/**
 * Created by kleist on 15/12/3.
 */

interface RssListViewer : Viewer {
    fun showItems(items : List<PodcastRSS>)
    fun jumpToAddNewPodcastView()
}

abstract class RssListPresenter : BasePresenter<RssListViewer>() {

    public abstract fun addPodcast()
    public abstract fun loadData()
}
