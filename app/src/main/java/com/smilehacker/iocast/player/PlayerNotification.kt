package com.smilehacker.iocast.player

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.NotificationCompat
import android.widget.RemoteViews
import com.smilehacker.iocast.App
import com.smilehacker.iocast.Config
import com.smilehacker.iocast.Constants
import com.smilehacker.iocast.R

/**
 * Created by kleist on 16/3/31.
 */
object PlayerNotification {


    public const val NOTIFICATION_ID = 100
    private val REQUEST_CODE_PRE = 1
    private val REQUEST_CODE_NEXT = 2
    private val REQUEST_CODE_PLAY = 3
    private val REQUEST_CODE_PAUSE = 6
    private val REQUEST_CODE_DESTROY = 4

    private var mNotificationManager : NotificationManager? = null
    private val mRemoteViews : RemoteViews by lazy { RemoteViews(Config.PACKAGE_NAME, R.layout.notification_player) }
    private val builder : NotificationCompat.Builder by lazy { NotificationCompat.Builder(App.inst) }

    private lateinit var mPlayPendingIntent : PendingIntent
    private lateinit var mPausePendingIntent : PendingIntent

    init {
        mNotificationManager = App.inst.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        prepare()
    }


    fun prepare() {
        val preIntent = Intent()
        preIntent.action = Constants.ACTION_PLAYER_NOTIFICATION
        preIntent.putExtra(Constants.KEY_PLAY_SERVICE_COMMAND, PlayService.COMMAND.PRE)
        val prePendingIntent = PendingIntent.getBroadcast(App.inst, REQUEST_CODE_PRE, preIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        mRemoteViews.setOnClickPendingIntent(R.id.iv_pre, prePendingIntent)

        val playIntent = Intent()
        playIntent.action = Constants.ACTION_PLAYER_NOTIFICATION
        playIntent.putExtra(Constants.KEY_PLAY_SERVICE_COMMAND, PlayService.COMMAND.START)
        mPlayPendingIntent = PendingIntent.getBroadcast(App.inst, REQUEST_CODE_PLAY, playIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val pauseIntent = Intent()
        pauseIntent.action = Constants.ACTION_PLAYER_NOTIFICATION
        pauseIntent.putExtra(Constants.KEY_PLAY_SERVICE_COMMAND, PlayService.COMMAND.PAUSE)
        mPausePendingIntent = PendingIntent.getBroadcast(App.inst, REQUEST_CODE_PAUSE, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val destroyIntent = Intent()
        destroyIntent.action = Constants.ACTION_PLAYER_NOTIFICATION
        destroyIntent.putExtra(Constants.KEY_PLAY_SERVICE_COMMAND, PlayService.COMMAND.STOP)
        val destroyPendingIntent = PendingIntent.getBroadcast(App.inst, REQUEST_CODE_DESTROY, destroyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        mRemoteViews.setOnClickPendingIntent(R.id.iv_close, destroyPendingIntent)
    }

    fun showPlayerNotification(title: String, author: String,
                               isPlaying: Boolean) : Notification {

        mRemoteViews.setTextViewText(R.id.tv_title, title)
        mRemoteViews.setTextViewText(R.id.tv_author, author)
        builder.mContentTitle = title
        builder.mContentText = author
        builder.setSmallIcon(R.drawable.play_circle_outline)

        if (isPlaying) {
            mRemoteViews.setImageViewResource(R.id.iv_play, R.drawable.pause_circle_outline_white)
            mRemoteViews.setOnClickPendingIntent(R.id.iv_play, mPausePendingIntent)
        } else {
            mRemoteViews.setImageViewResource(R.id.iv_play, R.drawable.play_circle_outline_white)
            mRemoteViews.setOnClickPendingIntent(R.id.iv_play, mPlayPendingIntent)
        }

        val notification = builder.build()
        notification.contentView = mRemoteViews
        notification.bigContentView = mRemoteViews
        notification.flags = NotificationCompat.FLAG_ONGOING_EVENT

        mNotificationManager?.notify(NOTIFICATION_ID, notification)
        return notification
    }

    fun updateNotificationAlbum(bitmap : Bitmap) {
        mRemoteViews.setImageViewBitmap(R.id.iv_album, bitmap)
    }

    fun removeNotification() {
        mNotificationManager?.cancel(NOTIFICATION_ID)
    }

}