package com.smilehacker.iocast.newPodcast

import android.text.TextUtils
import com.smilehacker.iocast.model.PodcastRSS
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

                .observeOn(Schedulers.computation())
                .map { rss -> PodcastRSS.parse(rss) }

                .observeOn( Schedulers.io())
                .map{ rss -> rss?.saveWithItems() }

                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { view?.jumpToPodcastView(it!!.id) },
                        { DLog.e(it) })

    }
}