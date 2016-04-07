package com.smilehacker.iocast.player.bottomplayer

import com.smilehacker.iocast.model.wrap.PodcastWrap
import com.smilehacker.iocast.player.PlayController
import com.smilehacker.iocast.util.RxBus
import rx.Subscription

/**
 * Created by kleist on 16/4/7.
 */

class BottomPlayerPresenterImpl : BottomPlayerPresenter() {

    private lateinit var mPlayerSubscription : Subscription

    override fun attachView(view: BottomPlayerViewer) {
        super.attachView(view)
        mPlayerSubscription = RxBus.toObservable(PodcastWrap::class.java)
                .subscribe { view.updateBottomPlayer(it) }

    }

    override fun detachView(retainInstance: Boolean) {
        super.detachView(retainInstance)
        mPlayerSubscription.unsubscribe()
    }

    override fun shouldShow(): Boolean {
        return if (PlayController.getLastPodcastWrap() != null) true else false
    }

}
