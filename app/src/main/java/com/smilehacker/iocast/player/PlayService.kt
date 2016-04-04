package com.smilehacker.iocast.player

import android.app.Notification
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import com.google.android.exoplayer.ExoPlaybackException
import com.google.android.exoplayer.ExoPlayer
import com.smilehacker.iocast.Constants
import com.smilehacker.iocast.model.PodcastItem
import com.smilehacker.iocast.model.player.PLAY_INFO_INTENT_FILTER
import com.smilehacker.iocast.model.player.PlayInfo
import com.smilehacker.iocast.model.player.PlayStatus
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
        when(command) {
            COMMAND.START -> start()
            COMMAND.PREPARE -> {
                val podcastItem = intent?.getParcelableExtra<PodcastItem>(Constants.KEY_PLAY_PODCAST_ITEM)
                if (podcastItem != null) {
                    mPodcastItem = podcastItem
                    prepare()
                }
            }
            COMMAND.PAUSE -> pause()
            COMMAND.STOP -> stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    fun prepare() {
        mPodcastItem?.fileUrl?.apply {
            val notification = showNotification(false)
            if (notification != null) {
                startForeground(PlayerNotification.NOTIFICATION_ID, notification)
            }
            mPlayer.prepare(mPodcastItem!!.fileUrl)
        }
    }


    fun start(pos : Long = 0) {
        if (mPodcastItem == null) {
            return
        }
        showNotification(true)
        mPlayer.start(pos)
    }

    fun pause() {
        if (mPodcastItem == null) {
            return
        }
        showNotification(false)
        mPlayer.pause()
    }

    fun seekTo(pos : Long) {
        if (mPodcastItem == null) {
            return
        }
        mPlayer.seekTo(pos)
    }

    fun showNotification(isPlaying : Boolean = false) : Notification? {
        mPodcastItem?.let {
            return PlayerNotification.showPlayerNotification(it.title, it.author, null, isPlaying)
        }
        return null
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        DLog.d("playWhenReady : $playWhenReady, playbackState: $playbackState")
        when(playbackState) {
            ExoPlayer.STATE_IDLE -> {
                showNotification(false)
            }
            ExoPlayer.STATE_BUFFERING-> {
                showNotification(false)
                mHandler.removeCallbacks(mGetPlayInfoRunnable)
            }
            ExoPlayer.STATE_PREPARING -> {
                showNotification(false)
                mHandler.removeCallbacks(mGetPlayInfoRunnable)
            }
            ExoPlayer.STATE_READY -> {
                showNotification(true)
                mHandler.removeCallbacks(mGetPlayInfoRunnable)
                broadcastPlayingInfo()
            }
            ExoPlayer.STATE_ENDED -> {
                showNotification(false)
                mHandler.removeCallbacks(mGetPlayInfoRunnable)
            }

        }
    }

    override fun onPlayWhenReadyCommitted() {
        DLog.d("onPlayWhenReadyCommitted")
    }

    override fun onPlayerError(error: ExoPlaybackException?) {
        DLog.d("onPlayError : ")
        DLog.e(error)
    }

    private fun broadcastPlayingInfo() {
        val status = PlayStatus.PLAYING
        val info = PlayInfo(mPodcastItem!!.title, mPodcastItem!!.author, null, mPlayer.getDuration(), mPlayer.getCurrentPosition(), status)
        val intent = Intent(PLAY_INFO_INTENT_FILTER)
        intent.putExtra(Constants.KEY_PLAY_INFO, info)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        mHandler.postDelayed(mGetPlayInfoRunnable, 1000)
    }

    private val mNotificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val command = intent?.getIntExtra(Constants.KEY_PLAY_SERVICE_COMMAND, COMMAND.PAUSE)
            when(command) {
                COMMAND.START -> start()
                COMMAND.PAUSE -> pause()
                COMMAND.STOP -> stopSelf()
            }
        }

    }
}