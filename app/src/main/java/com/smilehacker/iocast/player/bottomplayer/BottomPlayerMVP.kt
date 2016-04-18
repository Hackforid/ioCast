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

    fun show(show : Boolean)
}

abstract class BottomPlayerPresenter : BasePresenter<BottomPlayerViewer>() {
    abstract fun shouldShow() : Boolean
    abstract fun showPodcastItem()
    abstract fun getCurrentPodcast() : PodcastWrap?
    abstract fun play(play : Boolean)
}
