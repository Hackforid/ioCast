package com.smilehacker.iocast.player.bottomplayer

import com.smilehacker.iocast.base.mvp.BasePresenter
import com.smilehacker.iocast.base.mvp.Viewer
import com.smilehacker.iocast.model.wrap.PodcastWrap

/**
 * Created by kleist on 16/4/7.
 */

interface BottomPlayerViewer : Viewer {
    fun updateBottomPlayer(wrap: PodcastWrap)
    fun onResume()
    fun onPause()
}

abstract class BottomPlayerPresenter : BasePresenter<BottomPlayerViewer>() {
    abstract fun shouldShow() : Boolean
}