package com.smilehacker.iocast.download

import com.smilehacker.iocast.App
import com.smilehacker.iocast.Constants
import org.jetbrains.anko.startService

/**
 * Created by kleist on 16/2/16.
 */
object DownloadManager {

    fun start(podcastItemId : Long) {
        App.inst.startService<DownloadService>(Constants.KEY_PODCAST_ITEM_ID to podcastItemId,
                Constants.KEY_DOWNLOAD_SERVICE_COMMAND to DownloadService.COMMAND_START)
    }

    fun pause(podcastItemId: Long) {
        App.inst.startService<DownloadService>(Constants.KEY_PODCAST_ITEM_ID to podcastItemId,
                Constants.KEY_DOWNLOAD_SERVICE_COMMAND to DownloadService.COMMAND_PAUSE)
    }

}

