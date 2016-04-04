package com.smilehacker.iocast.player

import com.smilehacker.iocast.App
import com.smilehacker.iocast.Constants
import com.smilehacker.iocast.model.PodcastItem
import org.jetbrains.anko.startService

/**
 * Created by kleist on 16/3/23.
 */
object PlayManager {

    fun prepare(item : PodcastItem) {
        App.inst.startService<PlayService>(
                Constants.KEY_PLAY_SERVICE_COMMAND to PlayService.COMMAND.PREPARE,
                Constants.KEY_PLAY_PODCAST_ITEM to item)
    }

    fun start() {
        App.inst.startService<PlayService>(
                Constants.KEY_PLAY_SERVICE_COMMAND to PlayService.COMMAND.START)
    }

    fun pause() {
        App.inst.startService<PlayService>(
                Constants.KEY_PLAY_SERVICE_COMMAND to PlayService.COMMAND.PAUSE)
    }

    fun stop() {
        App.inst.startService<PlayService>(
                Constants.KEY_PLAY_SERVICE_COMMAND to PlayService.COMMAND.STOP)
    }
}