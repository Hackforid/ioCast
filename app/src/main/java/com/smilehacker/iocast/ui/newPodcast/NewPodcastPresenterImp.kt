package com.smilehacker.iocast.ui.newPodcast

import android.text.TextUtils
import com.smilehacker.iocast.cache.MemoryCache
import com.smilehacker.iocast.model.Podcast
import com.smilehacker.iocast.net.RssDownloader
import com.smilehacker.iocast.util.DLog
import com.smilehacker.iocast.util.url.prepareURL
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.toSingletonObservable
import rx.schedulers.Schedulers

/**
 * Created by kleist on 15/12/3.
 */
class NewPodcastPresenterImp : NewPodcastPresenter() {
    override fun addByUrl(url: String) {
        url.toSingletonObservable()
                .flatMap({ url ->
                    Observable.create(Observable.OnSubscribe<kotlin.String> { t ->
                        var rssStr : String? = null

                        try {
                            rssStr = RssDownloader().download(prepareURL(url))
                            DLog.d(rssStr)
                        } catch (e : Exception) {
                            t?.onError(e)
                        }

                        if (!TextUtils.isEmpty(rssStr)) {
                            t?.onNext(rssStr)
                        } else {
                            t?.onError(Exception("can't download rss"))
                        }
                    })
                })
                .subscribeOn(Schedulers.io())

                .observeOn(AndroidSchedulers.mainThread())
                .map { rss -> Podcast.parse(rss) }
                .subscribe(
                        {   it?.feedUrl = url
                            MemoryCache.cachePodcastRss(it)
                            view?.jumpToPodcastView() },
                        { DLog.e(it) })

    }
}