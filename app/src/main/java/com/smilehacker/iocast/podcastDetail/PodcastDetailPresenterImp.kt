package com.smilehacker.iocast.podcastDetail

import android.content.Intent
import com.smilehacker.iocast.Constants
import com.smilehacker.iocast.model.DownloadInfo
import com.smilehacker.iocast.download.PodcastDownloader
import com.smilehacker.iocast.model.PodcastRSS
import com.smilehacker.iocast.util.DLog

/**
 * Created by kleist on 15/12/14.
 */
class PodcastDetailPresenterImp : PodcastDetailPresenter() {

    override fun initData(intent: Intent) {
        val podcastID = intent.getLongExtra(Constants.KEY_PODCAST_ID, 0)
        podcast = PodcastRSS.getWithItems(podcastID)
    }

    override fun showPodcast() {
        view?.showPodcast(podcast)
    }

    override fun subscribePodcast() {
        podcast?.subscribe(true)
        view?.updateSubscribeStatus(podcast)
    }

    override fun downloadPodcast(itemUrl: String) {
        DLog.d("download podcast $itemUrl")
        val url = "http://192.168.1.15:8000/charles.jar"
        val downloadInfo = DownloadInfo()
        downloadInfo.url = itemUrl
        val downloader = PodcastDownloader(downloadInfo)
        DLog.d(downloader.getFileName(itemUrl))
    }

}