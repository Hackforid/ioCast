package com.smilehacker.iocast.player

import com.smilehacker.iocast.App
import com.smilehacker.iocast.Constants
import com.smilehacker.iocast.model.PodcastItem
import com.smilehacker.iocast.util.RxBus
import org.jetbrains.anko.startService

/**
 * Created by kleist on 16/3/23.
 */
object PlayManager {

    const val SIMPLE_STATE_PLAYING = 1
    const val SIMPLE_STATE_PAUSE = 2

    fun prepare(item : PodcastItem) {
        App.inst.startService<PlayService>(
                Constants.KEY_PLAY_SERVICE_COMMAND to PlayService.COMMAND.PREPARE,
                Constants.KEY_PLAY_PODCAST_ITEM to item)
    }

    fun start(pos : Long = 0) {
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

    fun postPlayState(item : PodcastItem, isPlaying : Boolean) {
        item.playState = if (isPlaying) SIMPLE_STATE_PLAYING else SIMPLE_STATE_PAUSE
        RxBus.post(item)
    }
}