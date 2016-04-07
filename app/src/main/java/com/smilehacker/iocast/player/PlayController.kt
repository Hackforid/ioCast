package com.smilehacker.iocast.player

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.smilehacker.iocast.App
import com.smilehacker.iocast.model.wrap.PodcastWrap
import com.smilehacker.iocast.store.PodcastStore
import com.smilehacker.iocast.store.UserStore
import com.smilehacker.iocast.util.DLog
import com.smilehacker.iocast.util.RxBus
import rx.Observable
import rx.subjects.PublishSubject

/**
 * Created by kleist on 16/3/23.
 */
object PlayController : ServiceConnection {

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        DLog.d("on bind service")
        mPlayService = (service as PlayService.PlayServiceBinder).getService()
        Observable.just(mPlayService).subscribe(mPubSubscription)
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        DLog.d("on disconnected")
        mPlayService = null
    }

    const val SIMPLE_STATE_PLAYING = 1
    const val SIMPLE_STATE_PAUSE = 2
    const val SIMPLE_STATE_STOP = 3

    var mPlayService : PlayService? = null
    var hideBottomPlayer = true

    var mPubSubscription : PublishSubject<PlayService> = PublishSubject.create()

    private fun bindPlayService() {
        val intent = Intent(App.inst, PlayService::class.java)
        App.inst.bindService(intent, this, Context.BIND_AUTO_CREATE)
    }

    fun unbindPlaySerivce() {
        mPlayService = null
        App.inst.unbindService(this)
    }

    fun prepare(podcastItemId: Long) {
        val prepareFunc : (PlayService)-> Unit = {service -> service.prepare(podcastItemId)}
        if (mPlayService == null) {
            bindPlayService()
            mPubSubscription.subscribe({
                prepareFunc(it)
            })
        } else {
            prepareFunc(mPlayService!!)
        }
    }

    fun prepareAndStart(podcastItemId: Long) {
        val prepareFunc : (PlayService)-> Unit = {service -> service.prepare(podcastItemId);service.start()}
        if (mPlayService == null) {
            bindPlayService()
            mPubSubscription.subscribe({
                prepareFunc(it)
            })
        } else {
            prepareFunc(mPlayService!!)
        }
    }

    fun start() {
        mPlayService?.start()
    }

    fun pause() {
        mPlayService?.pause()
    }

    fun stop() {
        mPlayService?.stop()
        unbindPlaySerivce()
        mPlayService = null
    }

    fun postPlayState(podcastWrap : PodcastWrap, isPlaying : Boolean) {
        podcastWrap.playState = if (isPlaying) SIMPLE_STATE_PLAYING else SIMPLE_STATE_PAUSE
        RxBus.post(podcastWrap)
    }

    fun getLastPodcastWrap() : PodcastWrap? {
        val lastPodcastId = UserStore.lastPodcastItemId
        if (lastPodcastId == -1L) {
            return null
        }
        return PodcastStore.getPodcastWrap(lastPodcastId)
    }

    fun setLastPodcastItemId(id : Long) {
        UserStore.lastPodcastItemId = id
    }


}