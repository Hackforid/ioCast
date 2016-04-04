package com.smilehacker.iocast.player

import android.app.Notification
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
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
import com.smilehacker.iocast.model.PodcastItem
import com.smilehacker.iocast.model.PodcastRSS
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

    private var mFileNetUrl : String? = null
    private var mPodcastItem : PodcastItem? = null
    private var mPodcastRss : PodcastRSS? = null

    private val mGetPlayInfoRunnable = Runnable { broadcastPlayingInfo() }

    init {
        mPlayer.setPlayListener(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException()
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
        val command = intent?.getIntExtra(Constants.KEY_PLAY_SERVICE_COMMAND, COMMAND.STOP)
        val podcastItem = intent?.getParcelableExtra<PodcastItem>(Constants.KEY_PLAY_PODCAST_ITEM)
        when(command) {
            COMMAND.PREPARE -> {
                if (podcastItem != null) {
                    mPodcastRss = PodcastRSS.get(podcastItem.podcastID)
                    mPodcastItem = podcastItem
                    mPodcastItem?.image = mPodcastRss?.image
                    prepare()
                }
            }
            COMMAND.START -> {
                mPodcastItem?.apply { start() }
            }
            COMMAND.PAUSE -> {
                mPodcastItem?.apply {
                    pause()
                }
            }
            COMMAND.STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun refreshNotification() {
        val notification = showNotification(mPlayer.isPlaying())
        if (notification != null) {
            startForeground(PlayerNotification.NOTIFICATION_ID, notification)
        }
    }

    private fun prepare() {
        mPodcastItem?.fileUrl?.apply {
            mPlayer.prepare(mPodcastItem!!.fileUrl)
            refreshNotification()
        }
    }


    private fun start(pos : Long = 0) {
        DLog.d("start")
        if (mPodcastItem == null) {
            return
        }
        mPlayer.start(pos)
        refreshNotification()
    }

    private fun pause() {
        DLog.d("pause")
        if (mPodcastItem == null) {
            return
        }
        mPlayer.pause()
        refreshNotification()
    }

    private fun seekTo(pos : Long) {
        if (mPodcastItem == null) {
            return
        }
        mPlayer.seekTo(pos)
        refreshNotification()
    }

    private fun stop() {
        mHandler.removeCallbacks(mGetPlayInfoRunnable)
        mPlayer.release()
        stopSelf()
    }


    fun showNotification(isPlaying : Boolean = false) : Notification? {
        mPodcastItem?.let {
            val noti = PlayerNotification.showPlayerNotification(it.title, it.author, isPlaying)
            getAlbum()
            return noti
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
            broadcastPlayingInfo()
        } else {
            mHandler.removeCallbacks(mGetPlayInfoRunnable)
        }
    }

    override fun onPlayerError(error: ExoPlaybackException?) {
        DLog.d("onPlayError : ")
        DLog.e(error)
    }

    private fun broadcastPlayingInfo() {
//        val status = PlayStatus.PLAYING
//        val info = PlayInfo(mPodcastItem!!.title, mPodcastItem!!.author, null, mPlayer.getDuration(), mPlayer.getCurrentPosition(), status)
//        val intent = Intent(PLAY_INFO_INTENT_FILTER)
//        intent.putExtra(Constants.KEY_PLAY_INFO, info)
//        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)

        if (mPodcastItem != null) {
        }
        mPodcastItem?.let {
            it.playedTime = mPlayer.getCurrentPosition()
            PlayManager.postPlayState(it, mPlayer.isPlaying())
        }
        mHandler.postDelayed(mGetPlayInfoRunnable, 1000)
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
        mPodcastItem ?: return
        DLog.d("get album podcast id = ${mPodcastItem?.podcastID}")
        val podcast = PodcastRSS.get(mPodcastItem!!.podcastID) ?: return

        val imageRequest = ImageRequest.fromUri(podcast.image)
        val imagePipeline = Fresco.getImagePipeline()
        val dataSource = imagePipeline.fetchDecodedImage(imageRequest, this)

        val subscriber = AlbumBitmapSubscriber(podcast.id)

        dataSource.subscribe(subscriber, CallerThreadExecutor.getInstance())
    }

    inner class AlbumBitmapSubscriber(val podcastID : Long) : BaseBitmapDataSubscriber() {
        override fun onNewResultImpl(bitmap: Bitmap?) {
            DLog.d("onResult")
            if (bitmap == null) {
                DLog.d("but null")
            }
            if (bitmap != null && mPodcastItem?.podcastID == podcastID) {
                PlayerNotification.updateNotificationAlbum(bitmap)
            }
        }

        override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage>>?) {
            val t = dataSource?.failureCause
            DLog.e(t)
        }
    }

}