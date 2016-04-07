package com.smilehacker.iocast.player

import android.app.Notification
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import com.facebook.common.executors.CallerThreadExecutor
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.DataSource
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.imagepipeline.request.ImageRequest
import com.google.android.exoplayer.ExoPlaybackException
import com.google.android.exoplayer.ExoPlayer
import com.smilehacker.iocast.Constants
import com.smilehacker.iocast.model.wrap.PodcastWrap
import com.smilehacker.iocast.store.PodcastStore
import com.smilehacker.iocast.util.DLog

/**
 * Created by kleist on 16/3/23.
 */
class PlayService : Service(), ExoPlayer.Listener {
    object COMMAND {
        const val PREPARE = 0
        const val START = 3
        const val PAUSE = 1
        const val STOP = 2

        const val NEXT = 4
        const val PRE = 5
    }



    private var mPlayer : Player = Player(this)
    private val mHandler : Handler = Handler()

    private var mPodcastWrap : PodcastWrap? = null

    private val mGetPlayInfoRunnable = Runnable { scheduleGetPlayState() }

    init {
        mPlayer.setPlayListener(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return PlayServiceBinder()
    }

    override fun onCreate() {
        super.onCreate()
        val notifiIntentFilter = IntentFilter(Constants.ACTION_PLAYER_NOTIFICATION)
        registerReceiver(mNotificationReceiver, notifiIntentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mNotificationReceiver)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        val command = intent?.getIntExtra(Constants.KEY_PLAY_SERVICE_COMMAND, COMMAND.STOP)
//        when(command) {
//            COMMAND.PREPARE -> {
//                val podcastItemId = intent?.getLongExtra(Constants.KEY_PODCAST_ITEM_ID, -1L)
//                if (podcastItemId != -1L) {
//                    mPodcastWrap = PodcastStore.getPodcastWrap(podcastItemId!!)
//                    if (mPodcastWrap == null) {
//                        stop()
//                    } else {
//                        prepare()
//                    }
//                } else {
//                    stop()
//                }
//            }
//            COMMAND.START -> {
//                start()
//            }
//            COMMAND.PAUSE -> {
//                pause()
//            }
//            COMMAND.STOP -> stop()
//        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun refreshNotification() {
        val notification = showNotification(mPlayer.isPlaying())
        if (notification != null) {
            startForeground(PlayerNotification.NOTIFICATION_ID, notification)
        }
    }

    fun prepare(podcastItemId : Long) {
        mPodcastWrap = PodcastStore.getPodcastWrap(podcastItemId)
        mPodcastWrap?.podcastItem?.fileUrl?.let {
            mPlayer.prepare(it)
            refreshNotification()
        }
    }


    fun start() {
        DLog.d("start")
        mPodcastWrap?.let {
            mPlayer.start(it.podcastItem.playedTime)
            refreshNotification()
        }
    }

    fun pause() {
        DLog.d("pause")
        mPlayer.pause()
        mPodcastWrap?.podcastItem?.updatePlayedTime(mPlayer.getCurrentPosition())
        refreshNotification()
    }

    fun seekTo(pos : Long) {
        mPlayer.seekTo(pos)
        refreshNotification()
    }

    fun stop() {
        mHandler.removeCallbacks(mGetPlayInfoRunnable)
        pause()
        mPlayer.release()

        stopSelf()
        PlayController.unbindPlaySerivce()
    }


    fun showNotification(isPlaying : Boolean = false) : Notification? {
        mPodcastWrap?.let {

            val notification = PlayerNotification.showPlayerNotification(it.podcastItem.title, it.podcastItem.author, isPlaying)
            getAlbum()
            return notification
        }
        return null
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        DLog.d("playWhenReady : $playWhenReady, playbackState: $playbackState")
        when(playbackState) {
            ExoPlayer.STATE_IDLE -> {
                showNotification(false)
            }
            ExoPlayer.STATE_PREPARING -> {
                showNotification(false)
            }
            ExoPlayer.STATE_BUFFERING-> {
                showNotification(false)
            }
            ExoPlayer.STATE_READY -> {
                showNotification(true)
            }
            ExoPlayer.STATE_ENDED -> {
                showNotification(false)
            }

        }
    }

    override fun onPlayWhenReadyCommitted() {
        DLog.d("onPlayWhenReadyCommitted isPlaying=${mPlayer.isPlaying()}")
        if (mPlayer.isPlaying()) {
            scheduleGetPlayState()
        } else {
            mHandler.removeCallbacks(mGetPlayInfoRunnable)
            broadcastPlayingInfo()
        }
    }

    override fun onPlayerError(error: ExoPlaybackException?) {
        DLog.d("onPlayError : ")
        DLog.e(error)
    }

    private var mLastUpdatePlayedTime : Long = 0L
    private fun scheduleGetPlayState() {
        mPodcastWrap?.let {
            val time = System.currentTimeMillis()
            if (time - mLastUpdatePlayedTime > 5000) {
                mLastUpdatePlayedTime = time
                it.podcastItem.updatePlayedTime(mPlayer.getCurrentPosition())
            } else {
                it.podcastItem.playedTime = mPlayer.getCurrentPosition()
            }
            broadcastPlayingInfo()
            mHandler.postDelayed(mGetPlayInfoRunnable, 1000)
        }
    }

    private fun broadcastPlayingInfo() {
        mPodcastWrap?.let {
            PlayController.postPlayState(it, mPlayer.isPlaying())
        }
    }

    private val mNotificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val command = intent?.getIntExtra(Constants.KEY_PLAY_SERVICE_COMMAND, COMMAND.PAUSE)
            DLog.d("receive notification command $command")
            when(command) {
                COMMAND.START -> start()
                COMMAND.PAUSE -> pause()
                COMMAND.STOP -> stop()
            }
        }

    }

    private fun getAlbum() {
        mPodcastWrap?.let {
            val imageRequest = ImageRequest.fromUri(it.podcast.image)
            val imagePipeline = Fresco.getImagePipeline()
            val dataSource = imagePipeline.fetchDecodedImage(imageRequest, this)

            val subscriber = AlbumBitmapSubscriber(it.podcast.id)

            dataSource.subscribe(subscriber, CallerThreadExecutor.getInstance())
        }
    }

    inner class AlbumBitmapSubscriber(val podcastID : Long) : BaseBitmapDataSubscriber() {
        override fun onNewResultImpl(bitmap: Bitmap?) {
            DLog.d("onResult")
            if (bitmap == null) {
                DLog.d("but null")
            }
            if (bitmap != null && mPodcastWrap?.podcast?.id == podcastID) {
                PlayerNotification.updateNotificationAlbum(bitmap)
            }
        }

        override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage>>?) {
            val t = dataSource?.failureCause
            DLog.e(t)
        }
    }

    inner class PlayServiceBinder : Binder() {
        fun getService() : PlayService {
            return this@PlayService
        }
    }

}