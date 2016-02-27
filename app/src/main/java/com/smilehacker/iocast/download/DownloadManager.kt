package com.smilehacker.iocast.download

import com.smilehacker.iocast.model.DownloadInfo
import java.util.*

/**
 * Created by kleist on 16/2/16.
 */
object DownloadManager {

    const val MAX_DOWNLOAD_TASK_SIZE = 4

    private val mDownloadList = ArrayList<PodcastDownloader>(MAX_DOWNLOAD_TASK_SIZE)
    private val mWaitList = ArrayList<DownloadInfo>()

    fun downloadPodcast(url : String) {
        var downloadInfo = DownloadInfo.get(url)
        if (downloadInfo == null) {
            downloadInfo = DownloadInfo(url)
        }

        pushToDownload(downloadInfo)
    }


    fun isTaskInDownloadList(url : String) : Boolean {
        return -1 != mDownloadList.indexOfFirst { x -> x.url.equals(url) }
    }

    fun isTaskInWaitingList(url : String) : Boolean {
        return -1 != mWaitList.indexOfFirst { x -> x.url.equals(url) }
    }

    fun pushToDownload(downloadInfo : DownloadInfo) {
        if (mDownloadList.size < MAX_DOWNLOAD_TASK_SIZE) {
            if (!isTaskInDownloadList(downloadInfo.url)) {
                val downloader = PodcastDownloader(downloadInfo)
                mDownloadList.add(downloader)
                downloader.start()
            }
        } else {
            if (!isTaskInWaitingList(downloadInfo.url)) {
                mWaitList.add(downloadInfo)
            }
        }
    }


}

