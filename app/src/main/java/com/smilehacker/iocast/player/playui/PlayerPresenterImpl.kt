package com.smilehacker.iocast.player.playui

import com.smilehacker.iocast.model.wrap.PodcastWrap
import com.smilehacker.iocast.player.PlayController
import com.smilehacker.iocast.util.RxBus
import rx.Subscription

/**
 * Created by kleist on 16/4/17.
 */
class PlayerPresenterImpl : PlayerPresenter() {

    private lateinit var mPlayerSubscription : Subscription

    private var mPodcast : PodcastWrap? = null


    override fun getCurrentPodcast(): PodcastWrap? {
        val podcastWrap = PlayController.getLastPodcastWrap()
        mPodcast = podcastWrap
        return podcastWrap
    }

    override fun attachView(view: PlayerViewer) {
        super.attachView(view)
        mPlayerSubscription = RxBus.toObservable(PodcastWrap::class.java)
                .subscribe {
                    mPodcast = it
                    this.view?.showPodcast(it)
                }
    }

    override fun detachView(retainInstance: Boolean) {
        super.detachView(retainInstance)
        mPlayerSubscription.unsubscribe()
    }

    override fun setPos(current: Long) {
        PlayController.seekTo(current)
    }

    override fun play(play: Boolean) {
        mPodcast?.apply {
            if (play) {
                PlayController.play(podcastItem.id)
            } else {
                PlayController.pause()
            }
        }
    }
}