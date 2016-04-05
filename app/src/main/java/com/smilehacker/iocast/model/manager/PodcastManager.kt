package com.smilehacker.iocast.model.manager

import com.smilehacker.iocast.model.Podcast
import com.smilehacker.iocast.model.PodcastItem
import com.smilehacker.iocast.model.wrap.PodcastWrap

/**
 * Created by kleist on 16/4/4.
 */
object PodcastManager {

    fun getPodcastWithItems(id : Long) : Podcast? {
        return Podcast.getWithItems(id)
    }

    fun getPodcastWrap(podcastItemId : Long) : PodcastWrap? {
        val podcastItem = PodcastItem.get(podcastItemId) ?: return null
        val podcast = Podcast.get(podcastItem.podcastID) ?: return null
        val wrap = PodcastWrap(podcast, podcastItem)
        return wrap
    }
}