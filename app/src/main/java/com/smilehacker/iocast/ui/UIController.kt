package com.smilehacker.iocast.ui

import com.smilehacker.iocast.model.event.UIEvent
import com.smilehacker.iocast.player.PlayController
import com.smilehacker.iocast.util.RxBus

/**
 * Created by kleist on 16/4/7.
 */
object UIController {

    var isBottomPlayerShow : Boolean = false
        get() = mShowBottomPlayer && PlayController.getLastPodcastWrap() != null
        private set

    private var mShowBottomPlayer = true

    fun showBottomPlayer(show : Boolean) {
        mShowBottomPlayer = show
        val command = if (isBottomPlayerShow) UIEvent.SHOW_BOTTOM_PLAYER else UIEvent.HIDE_BOTTOM_PLAYER
        RxBus.post(UIEvent(UIEvent.TYPE_SHOW_BOTTOM_PLAYER, command))
    }

}

