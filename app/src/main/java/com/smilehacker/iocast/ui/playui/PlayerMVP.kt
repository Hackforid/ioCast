package com.smilehacker.iocast.ui.playui

import com.smilehacker.iocast.base.mvp.BasePresenter
import com.smilehacker.iocast.base.mvp.Viewer
import com.smilehacker.iocast.model.wrap.PodcastWrap

/**
 * Created by kleist on 16/4/17.
 */

interface PlayerViewer : Viewer {
    fun showPodcast(podcastWrap: PodcastWrap)
    fun updateProgress(total : Long, current : Long)
}

abstract class PlayerPresenter : BasePresenter<PlayerViewer>() {
    abstract fun getCurrentPodcast() : PodcastWrap?
    abstract fun setPos(current : Long)
    abstract fun play(play : Boolean)
}
