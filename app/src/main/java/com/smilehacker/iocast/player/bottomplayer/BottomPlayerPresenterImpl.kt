package com.smilehacker.iocast.player.bottomplayer

import com.smilehacker.iocast.model.event.UIEvent
import com.smilehacker.iocast.model.wrap.PodcastWrap
import com.smilehacker.iocast.player.PlayController
import com.smilehacker.iocast.ui.UIController
import com.smilehacker.iocast.util.RxBus
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers

/**
 * Created by kleist on 16/4/7.
 */

class BottomPlayerPresenterImpl : BottomPlayerPresenter() {

    private lateinit var mPlayerSubscription : Subscription
    private lateinit var mUISubscription : Subscription

    override fun attachView(view: BottomPlayerViewer) {
        super.attachView(view)
        refreshUI()
        mPlayerSubscription = RxBus.toObservable(PodcastWrap::class.java)
                .subscribe { view.updateBottomPlayer(it) }
        mUISubscription = RxBus.toObservable(UIEvent::class.java)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { e -> onUIEvent(e) }

    }

    override fun detachView(retainInstance: Boolean) {
        super.detachView(retainInstance)
        mPlayerSubscription.unsubscribe()
        mUISubscription.unsubscribe()
    }

    override fun showPodcastItem() {
        val podcastWrap = PlayController.getLastPodcastWrap()
        podcastWrap?.apply {
            view?.updateBottomPlayer(podcastWrap)
        }
    }

    override fun shouldShow(): Boolean {
        return UIController.isBottomPlayerShow
    }


    private fun onUIEvent(event : UIEvent) {
        when(event.type) {
            UIEvent.TYPE_SHOW_BOTTOM_PLAYER -> {
                if (event.value == UIEvent.SHOW_BOTTOM_PLAYER) {
                    view?.show(true)
                } else {
                    view?.show(false)
                }
            }
        }
    }

    private fun refreshUI() {
        if (UIController.isBottomPlayerShow) {
            view?.show(true)
            getCurrentPodcast()?.let { view?.updateBottomPlayer(it) }
        } else {
            view?.show(false)
        }
    }

    override fun getCurrentPodcast(): PodcastWrap? {
        val podcastWrap = PlayController.getLastPodcastWrap()
        return podcastWrap
    }
}
