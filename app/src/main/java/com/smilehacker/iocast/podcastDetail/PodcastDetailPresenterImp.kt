package com.smilehacker.iocast.podcastDetail

import android.content.Intent
import com.smilehacker.iocast.Constants
import com.smilehacker.iocast.model.PodcastRSS

/**
 * Created by kleist on 15/12/14.
 */
class PodcastDetailPresenterImp : PodcastDetailPresenter() {

    override fun initData(intent: Intent) {
        val podcastID = intent.getLongExtra(Constants.KEY_PODCAST_ID, 0)
        podcast = PodcastRSS.getWithItems(podcastID)
    }

    override fun showPodcast() {
        view?.showPodcast(podcast)
    }

    override fun subscribePodcast() {
        podcast?.subscribe(true)
        view?.updateSubscribeStatus(podcast)
    }

}