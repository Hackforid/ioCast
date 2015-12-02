package com.smilehacker.iocast.rsslist

import com.smilehacker.iocast.base.mvp.BasePresenter
import com.smilehacker.iocast.model.PodcastRSS

/**
 * Created by kleist on 15/12/2.
 */
class RssListPresenter : BasePresenter<RssListViewer>(), IRssListPresenter {


    public override fun loadData() {
        val items = PodcastRSS.getAll()
        view?.showItems(items)
    }

    public fun addPodcast() {

    }

}