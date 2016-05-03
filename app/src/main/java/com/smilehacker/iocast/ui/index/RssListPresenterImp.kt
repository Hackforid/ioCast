package com.smilehacker.iocast.ui.index

import com.smilehacker.iocast.model.Podcast
import com.smilehacker.iocast.util.DLog

/**
 * Created by kleist on 15/12/3.
 */
class RssListPresenterImp : RssListPresenter() {
    override fun onRssItemClick(rss: Podcast) {
        view?.jumpToPodcastView(rss.id)
    }

    override fun loadData() {
        val items = Podcast.getAll(subscribed = true)
        DLog.d("load items size=${items.size}")
        view?.showItems(items)
    }

    override fun addPodcast() {
        view?.jumpToAddNewPodcastView()
    }
}