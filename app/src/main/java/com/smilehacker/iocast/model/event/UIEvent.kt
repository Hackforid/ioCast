package com.smilehacker.iocast.model.event

/**
 * Created by kleist on 16/4/14.
 */

data class UIEvent(val type : Int, val value : Int) {
    companion object {
        val TYPE_SHOW_BOTTOM_PLAYER = 1

        val SHOW_BOTTOM_PLAYER = 1
        val HIDE_BOTTOM_PLAYER = 1
    }
}
