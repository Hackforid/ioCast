package com.smilehacker.iocast.cache

import com.smilehacker.iocast.model.Podcast
import java.lang.ref.WeakReference

/**
 * Created by kleist on 16/2/27.
 */
object MemoryCache : Cache {

    private var mRss : WeakReference<Podcast>? = null

    override fun get(key: String): String? {
        throw UnsupportedOperationException()
    }

    override fun put(key: String, value: String) {
        throw UnsupportedOperationException()
    }

    fun cachePodcastRss(rss : Podcast?) {
        mRss = WeakReference<Podcast>(rss)
    }

    fun getPodcastRss() : Podcast? {
        return mRss?.get()
    }
}