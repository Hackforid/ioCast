package com.smilehacker.iocast.podcastDetail

import android.os.Bundle
import com.smilehacker.iocast.base.mvp.BasePresenter
import com.smilehacker.iocast.base.mvp.Viewer
import com.smilehacker.iocast.model.PodcastItem
import com.smilehacker.iocast.model.PodcastRSS

/**
 * Created by kleist on 15/12/14.
 */


interface PodcastDetailViewer : Viewer {
    abstract fun showPodcast(podcast : PodcastRSS?)
    abstract fun updateSubscribeStatus(podcast: PodcastRSS?)
    abstract fun startDownload(podcastItemId : Long);
    abstract fun updateDownloadStatus()
}


abstract class PodcastDetailPresenter : BasePresenter<PodcastDetailViewer>() {
    lateinit var podcast : PodcastRSS
    abstract fun initData(bundle: Bundle)
    abstract fun showPodcast()
    abstract fun subscribePodcast()
    abstract fun startDownload(item : PodcastItem)
    abstract fun pauseDownload(item: PodcastItem)
    abstract fun startPlay(item : PodcastItem)
}
