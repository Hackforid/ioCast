package com.smilehacker.iocast.podcastDetail

import android.content.Intent
import com.smilehacker.iocast.base.mvp.BasePresenter
import com.smilehacker.iocast.base.mvp.Viewer
import com.smilehacker.iocast.model.PodcastRSS

/**
 * Created by kleist on 15/12/14.
 */


interface PodcastDetailViewer : Viewer {
    abstract fun showPodcast(podcast : PodcastRSS?)
    abstract fun updateSubscribeStatus(podcast: PodcastRSS?)
}


abstract class PodcastDetailPresenter : BasePresenter<PodcastDetailViewer>() {
    public var podcast : PodcastRSS? = null
    abstract fun initData(intent : Intent)
    abstract fun showPodcast()
    abstract fun subscribePodcast()
    abstract fun downloadPodcast(itemUrl: String)
}
