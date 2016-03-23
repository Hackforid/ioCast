package com.smilehacker.iocast.download

import com.smilehacker.iocast.model.DownloadInfo

/**
 * Created by kleist on 16/3/18.
 */
data class DownloadEvent(val podcastItemId: Long,
                         val totalSize: Long,
                         val completeSize: Long,
                         val status: Int) {
    constructor(downloadInfo : DownloadInfo, status : Int) : this(downloadInfo.podcastItemID, downloadInfo.totalSize, downloadInfo.completeSize, status)
}