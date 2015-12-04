package com.smilehacker.iocast.newPodcast

import com.smilehacker.iocast.base.mvp.BasePresenter
import com.smilehacker.iocast.base.mvp.Viewer

/**
 * Created by kleist on 15/12/3.
 */

interface NewPodcastViewer : Viewer {
    fun jumpToPodcastView(id : Long)
}

abstract class NewPodcastPresenter : BasePresenter<NewPodcastViewer>() {
    abstract fun addByUrl(url : String)
}
