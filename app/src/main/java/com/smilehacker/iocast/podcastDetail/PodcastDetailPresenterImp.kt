package com.smilehacker.iocast.podcastDetail

import android.os.Bundle
import com.smilehacker.iocast.Constants
import com.smilehacker.iocast.cache.MemoryCache
import com.smilehacker.iocast.download.DownloadEvent
import com.smilehacker.iocast.download.DownloadManager
import com.smilehacker.iocast.model.DownloadInfo
import com.smilehacker.iocast.model.Podcast
import com.smilehacker.iocast.model.PodcastItem
import com.smilehacker.iocast.player.PlayController
import com.smilehacker.iocast.store.PodcastStore
import com.smilehacker.iocast.util.DLog
import com.smilehacker.iocast.util.RxBus
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.PublishSubject
import java.util.concurrent.TimeUnit

/**
 * Created by kleist on 15/12/14.
 */
class PodcastDetailPresenterImp : PodcastDetailPresenter() {

    private lateinit var mDownloadSubscription : Subscription
    private val mOperSubject = PublishSubject<DownloadEvent>()

    override fun initData(bundle: Bundle) {
        val type = bundle.getInt(Constants.KEY_PODCAST_TYPE, Constants.PODCAST_TYPE.ID)
        var _podcast : Podcast? = null
        when(type) {
            Constants.PODCAST_TYPE.ID -> _podcast = PodcastStore.getPodcastWithItems(bundle.getLong(Constants.KEY_PODCAST_ID, 0))
            Constants.PODCAST_TYPE.MEM -> _podcast = MemoryCache.getPodcastRss()
        }
        if (_podcast == null) {
            // TODO 报错
        } else {
            this.podcast = _podcast
        }
        checkRssExist()
        updatePodcastWithDownloadInfo()
    }

    private fun checkRssExist() {
        val podcastInDB = Podcast.getByFeedUrl(podcast.feedUrl)
        if (podcastInDB != null) {
            podcastInDB.update(podcast, true)
            podcast = podcastInDB
        }
    }

    override fun showPodcast() {
        view?.showPodcast(podcast)
    }

    override fun subscribePodcast() {
        podcast.subscribe(true)
        view?.updateSubscribeStatus(podcast)
    }


    override fun startDownload(item: PodcastItem) {
        DLog.d("download podcast ${item.fileUrl}")
        savePodcastRss()
        DownloadManager.start(item.id)
    }

    override fun pauseDownload(item: PodcastItem) {
        DownloadManager.pause(item.id)
    }

    private fun savePodcastRss() {
        val podcastInDB = Podcast.getByFeedUrl(podcast.feedUrl)
        if (podcastInDB == null) {
            podcast.saveWithItems()
        } else {
            podcastInDB.update(podcast, true)
            podcast = podcastInDB
        }
    }

    override fun onShow() {
        super.onShow()
        updatePodcastWithDownloadInfo()
        mDownloadSubscription = RxBus.toObservable(DownloadEvent::class.java)
                .buffer(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .filter { !it.isEmpty() }
                .subscribe { event -> updateDownloadInfos(event)  };
    }

    override fun onHidden() {
        super.onHidden()
        if (!mDownloadSubscription.isUnsubscribed) {
            mDownloadSubscription.unsubscribe()
        }

    }

    private fun updateDownloadInfos(events: List<DownloadEvent>) {
        DLog.d("event $events")
        podcast.items?.forEach {
            item -> events.find { it.podcastItemId == item.id }?.let {
                item.totalSize = it.totalSize
                item.completeSize = it.completeSize
                item.downloadStatus = it.status
            }
        }
        view?.showPodcast(podcast)
    }


    private fun updatePodcastWithDownloadInfo() {
        val podcastItemIds = podcast.items?.filter { it.id != 0L }?.map { it.id }?.toMutableList()
        podcastItemIds ?: return

        val downloadInfos = DownloadInfo.getByPodcastItemIds(podcastItemIds)
        DLog.d("get downloadinfos $downloadInfos")
        downloadInfos.forEach {
            downloadInfo ->
            podcast.items?.find { it.id == downloadInfo.podcastItemID  }?.setDownloadInfo(downloadInfo.totalSize, downloadInfo.completeSize, downloadInfo.status)
        }

    }

    override fun startPlay(item: PodcastItem) {
        PlayController.prepareAndStart(item.id)
    }
}