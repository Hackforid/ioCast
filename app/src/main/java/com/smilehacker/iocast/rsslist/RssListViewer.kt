package com.smilehacker.iocast.rsslist

import com.smilehacker.iocast.base.mvp.Viewer
import com.smilehacker.iocast.model.PodcastRSS

/**
 * Created by kleist on 15/12/2.
 */
interface RssListViewer : Viewer {
    fun showItems(items : List<PodcastRSS>)
}