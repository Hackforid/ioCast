package com.smilehacker.iocast.player

import com.smilehacker.iocast.App
import com.smilehacker.iocast.Constants
import com.smilehacker.iocast.model.wrap.PodcastWrap
import com.smilehacker.iocast.util.RxBus
import org.jetbrains.anko.startService

/**
 * Created by kleist on 16/3/23.
 */
object PlayManager {

    const val SIMPLE_STATE_PLAYING = 1
    const val SIMPLE_STATE_PAUSE = 2

    fun prepare(podcastItemId: Long) {
        App.inst.startService<PlayService>(
                Constants.KEY_PLAY_SERVICE_COMMAND to PlayService.COMMAND.PREPARE,
                Constants.KEY_PODCAST_ITEM_ID to podcastItemId)
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

    fun postPlayState(podcastWrap : PodcastWrap, isPlaying : Boolean) {
        podcastWrap.playState = if (isPlaying) SIMPLE_STATE_PLAYING else SIMPLE_STATE_PAUSE
        RxBus.post(podcastWrap)
    }
}