package com.smilehacker.iocast.podcastDetail

import android.content.Intent
import com.smilehacker.iocast.Constants
import com.smilehacker.iocast.cache.MemoryCache
import com.smilehacker.iocast.download.PodcastDownloader
import com.smilehacker.iocast.model.DownloadInfo
import com.smilehacker.iocast.model.PodcastItem
import com.smilehacker.iocast.model.PodcastRSS
import com.smilehacker.iocast.util.DLog

/**
 * Created by kleist on 15/12/14.
 */
class PodcastDetailPresenterImp : PodcastDetailPresenter() {

    override fun initData(intent: Intent) {
        val type = intent.getIntExtra(Constants.KEY_PODCAST_TYPE, Constants.PODCAST_TYPE.ID)
        var _podcast : PodcastRSS? = null
        when(type) {
            Constants.PODCAST_TYPE.ID -> _podcast = PodcastRSS.get(intent.getLongExtra(Constants.KEY_PODCAST_ID, 0))
            Constants.PODCAST_TYPE.MEM -> _podcast = MemoryCache.getPodcastRss()
        }
        if (_podcast == null) {
            // TODO 报错
        } else {
            this.podcast = _podcast
        }
        checkRssExist()
    }

    private fun checkRssExist() {
        val podcastInDB = PodcastRSS.getByFeedUrl(podcast.feedUrl)
        if (podcastInDB != null) {
            podcastInDB.update(podcast, true)
            podcast = podcastInDB
        }
    }

    override fun showPodcast() {
        view?.showPodcast(podcast)
    }

    override fun subscribePodcast() {
        podcast.subscribe(true)
        view?.updateSubscribeStatus(podcast)
    }

    override fun downloadPodcast(itemUrl: String) {
        DLog.d("download podcast $itemUrl")
        val downloadInfo = DownloadInfo()
        downloadInfo.url = itemUrl
        val downloader = PodcastDownloader(downloadInfo)
        DLog.d(downloader.getFileName(itemUrl))
    }

    override fun downloadPodcast(item: PodcastItem) {
        savePodcastRss()
    }

    private fun savePodcastRss() {
        val podcastInDB = PodcastRSS.getByFeedUrl(podcast.feedUrl)
        if (podcastInDB == null) {
            podcast.saveWithItems()
        } else {
            podcastInDB.update(podcast, true)
            podcast = podcastInDB
        }
    }
}