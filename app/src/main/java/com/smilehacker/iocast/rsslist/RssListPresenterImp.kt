package com.smilehacker.iocast.rsslist

import com.smilehacker.iocast.model.PodcastRSS
import com.smilehacker.iocast.util.DLog

/**
 * Created by kleist on 15/12/3.
 */
class RssListPresenterImp : RssListPresenter() {
    override fun onRssItemClick(rss: PodcastRSS) {
        view?.jumpToPodcastView(rss.id)
    }

    override fun loadData() {
        val items = PodcastRSS.getAll(subscribed = true)
        DLog.d("load items size=${items.size}")
        view?.showItems(items)
    }

    override fun addPodcast() {
        view?.jumpToAddNewPodcastView()
    }
}