package com.smilehacker.iocast.download

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import com.smilehacker.iocast.Constants
import com.smilehacker.iocast.model.DownloadInfo
import com.smilehacker.iocast.model.PodcastItem
import com.smilehacker.iocast.util.DLog
import com.smilehacker.iocast.util.RxBus
import java.util.*

/**
 * Created by kleist on 16/3/18.
 */
class DownloadService : Service() {

    companion object {
        val COMMAND_START = 1
        val COMMAND_PAUSE = 2
        val COMMAND_DELETE =3
        val COMMAND_TERMINATE = 4;
    }

    private val MIN_SAVE_INTERVAL = 5000

    private val mDownloadList = ArrayList<Pair<DownloadInfo, PodcastDownloader>>()

    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if (msg == null) {
                return
            }
            when (msg.what) {
                PodcastDownloader.MSG_TYPE_COMPLETE -> {
                    handleComplete(msg.obj as Long)
                }
                PodcastDownloader.MSG_TYPE_FAIL -> {
                    handleFailed(msg.obj as Long)
                }
                PodcastDownloader.MSG_TYPE_TOTAL_SIZE -> {
                    val arg = msg.obj as Pair<Long, Long>
                    handleTotalSize(arg.first, arg.second)
                }
                PodcastDownloader.MSG_TYPE_COMPLETE_SIZE -> {
                    val arg = msg.obj as Pair<Long, Long>
                    handleCurrentSize(arg.first, arg.second)
                }
                PodcastDownloader.MSG_TYPE_PAUSE -> {
                    handlePause(msg.obj as Long)
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        DLog.d("service start")
        if (intent == null) {
            stop()
            return super.onStartCommand(intent, flags, startId)
        }

        val command = intent.getIntExtra(Constants.KEY_DOWNLOAD_SERVICE_COMMAND, COMMAND_TERMINATE)
        when (command) {
            COMMAND_TERMINATE -> {
                terminate()
            }
            COMMAND_START -> {
                val podcastItemId = intent.getLongExtra(Constants.KEY_PODCAST_ITEM_ID, -1L)
                start(podcastItemId)
            }
            COMMAND_PAUSE -> {
                val podcastItemId = intent.getLongExtra(Constants.KEY_PODCAST_ITEM_ID, -1L)
                pause(podcastItemId)
            }
        }


        return super.onStartCommand(intent, flags, startId)
    }

    private fun start(podcastItemId: Long) {
        if (podcastItemId != -1L) {
            download(podcastItemId)
        }
    }

    private fun pause(podcastItemId: Long) {
        mDownloadList.find { it.first.podcastItemID == podcastItemId }?.apply { second.pause() }
    }

    private fun terminate() {
        mDownloadList.forEach {
            it.second.pause()
        }
        stop()
    }

    private fun download(podcastItemId : Long) {
        val podcastItem = PodcastItem.get(podcastItemId) ?: return

        mDownloadList.forEach {
            if (it.first.podcastItemID == podcastItemId) {
                return
            }
        }

        var downloadInfo = DownloadInfo.get(podcastItemId)
        if (downloadInfo == null) {
            downloadInfo = DownloadInfo()
            downloadInfo.url = podcastItem.fileUrl
            downloadInfo.podcastItemID = podcastItem.id
            downloadInfo.save()
        }
        val downloader = PodcastDownloader(mHandler, downloadInfo)
        mDownloadList.add(Pair(downloadInfo, downloader))
        downloader.start()
    }

    private fun stop() {
        DLog.d("DownloadService finish self")
        stopSelf()
    }

    private fun checkShouldStopSelf() {
        if (mDownloadList.isEmpty()) {
            stop()
        } else {
        }

    }

    private fun handleComplete(podcastItemId : Long) {

        mDownloadList.find { it.first.podcastItemID == podcastItemId }?.let {
            it.first.finishDownload()
            RxBus.post(DownloadEvent(it.first, it.second.status))
            mDownloadList.remove(it)
            checkShouldStopSelf()
        }

    }

    private fun handleFailed(podcastItemId: Long) {
        val pair = mDownloadList.find { it.first.podcastItemID == podcastItemId }
        if (pair != null) {
            RxBus.post(DownloadEvent(pair.first, pair.second.status))
            mDownloadList.remove(pair)
        }
        checkShouldStopSelf()
    }

    private fun handleTotalSize(podcastItemId: Long, totalSize : Long) {
        mDownloadList.forEach {
            if (it.first.podcastItemID == podcastItemId) {
                it.first.updateTotalSize(totalSize)
                RxBus.post(DownloadEvent(it.first, it.second.status))
                return@forEach
            }
        }
    }

    private fun handleCurrentSize(podcastItemId: Long, currentSize : Long) {
        mDownloadList.find { it.first.podcastItemID == podcastItemId }?.let {
            val time = System.currentTimeMillis()
            if (time - it.first.lastUpdateTime > MIN_SAVE_INTERVAL) {
                it.first.updateCompleteSize(currentSize)
                it.first.lastUpdateTime = time

                RxBus.post(DownloadEvent(it.first, it.second.status))
            }
        }
    }

    private fun handlePause(podcastItemId: Long) {

        mDownloadList.find { it.first.podcastItemID == podcastItemId }?.let {
            it.first.status = it.second.status
            RxBus.post(DownloadEvent(it.first, it.second.status))
            mDownloadList.remove(it)
        }

        checkShouldStopSelf()
    }
}