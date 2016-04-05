package com.smilehacker.iocast.download

import com.smilehacker.iocast.base.mvp.BasePresenter
import com.smilehacker.iocast.base.mvp.Viewer
import com.smilehacker.iocast.model.DownloadInfo
import com.smilehacker.iocast.model.PodcastItem
import com.smilehacker.iocast.model.Podcast

/**
 * Created by kleist on 16/2/18.
 */

interface DownloadViewer : Viewer {
    abstract fun showFileList(list : MutableList<PodcastFile>)
}

abstract class DownloadPresenter : BasePresenter<DownloadViewer>() {
    abstract fun getFileList() : MutableList<PodcastFile>
    abstract fun startDownload()
    abstract fun pauseDownload()
    abstract fun deleteFile()
    abstract fun playFile()
}

data class PodcastFile(val podcast : Podcast,
                       val item : PodcastItem,
                       val downloadInfo : DownloadInfo)