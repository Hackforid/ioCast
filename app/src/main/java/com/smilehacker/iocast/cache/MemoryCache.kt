package com.smilehacker.iocast.cache

import com.smilehacker.iocast.model.PodcastRSS
import java.lang.ref.WeakReference

/**
 * Created by kleist on 16/2/27.
 */
object MemoryCache : Cache {

    private var mRss : WeakReference<PodcastRSS>? = null

    override fun get(key: String): String? {
        throw UnsupportedOperationException()
    }

    override fun put(key: String, value: String) {
        throw UnsupportedOperationException()
    }

    fun cachePodcastRss(rss : PodcastRSS?) {
        mRss = WeakReference<PodcastRSS>(rss)
    }

    fun getPodcastRss() : PodcastRSS? {
        return mRss?.get()
    }
}