package com.smilehacker.iocast.download

import android.os.Handler
import com.smilehacker.iocast.App
import com.smilehacker.iocast.model.DownloadInfo
import com.smilehacker.iocast.util.DLog
import java.io.File
import java.io.RandomAccessFile
import java.net.URLEncoder


/**
 * Created by kleist on 16/2/17.
 */
class PodcastDownloader(
        val handler: Handler, val mPodcastItemId: Long = -1, val mUrl: String,
        val mCompleteSize: Long = 0, val mTotalSize: Long = 0)
: Downloader(mUrl, mCompleteSize, mTotalSize) {

    constructor(handler: Handler, downloadInfo: DownloadInfo)
    : this(handler, downloadInfo.podcastItemID, downloadInfo.url,
            downloadInfo.completeSize, downloadInfo.totalSize) {
    }

    companion object {
        val MSG_TYPE_TOTAL_SIZE = 1
        val MSG_TYPE_COMPLETE_SIZE = 2
        val MSG_TYPE_FAIL = 3
        val MSG_TYPE_COMPLETE = 4
        val MSG_TYPE_PAUSE = 5
    }

    override fun onGetTotalFileSize(totalSize: Long) {
        val msg = handler.obtainMessage(MSG_TYPE_TOTAL_SIZE)
        msg.obj = Pair(mPodcastItemId, totalSize)
        msg.sendToTarget()
    }

    private val MIN_MSG_TIME_INTERVAL = 1000

    private var mCurrentTime: Long = 0

    override fun onPause() {
        DLog.d("download pause")
        val msg = handler.obtainMessage(MSG_TYPE_PAUSE)
        msg.obj = mPodcastItemId
        msg.sendToTarget()
    }


    override fun onFail(e: Exception) {
        DLog.d("download fail 233333")
        DLog.e(e)
        val msg = handler.obtainMessage(MSG_TYPE_FAIL)
        msg.obj = mPodcastItemId
        msg.sendToTarget()
    }

    override fun onComplete() {
        DLog.d("download complete")
        val msg = handler.obtainMessage(MSG_TYPE_COMPLETE)
        msg.obj = mPodcastItemId
        msg.sendToTarget()
    }

    override fun onProcess(currentSize: Long, totalSize: Long) {
        val msg = handler.obtainMessage(MSG_TYPE_COMPLETE_SIZE)
        msg.obj = Pair(mPodcastItemId, currentSize)
        msg.sendToTarget()

        val time = System.currentTimeMillis()
        if (time - mCurrentTime > MIN_MSG_TIME_INTERVAL) {
        }
        mCurrentTime = time
    }

    override fun getTargetFile(url: String): RandomAccessFile? {
        val dir = makePodcastDIR() ?: return null

        val fileName = dir + "/" + getFileName(url)
        DLog.d("mk file at " + fileName.toString())
        var file: RandomAccessFile? = null
        try {
            file = RandomAccessFile(fileName, "rwd")
        } catch (e: Exception) {
            DLog.e(e)
        }

        return file
    }

    fun getFileName(url: String): String {
        var urlPrefixName = URLEncoder.encode(url, "utf8")
        if (!urlPrefixName.contains('.')) {
            urlPrefixName += ".mp3"
        }
        return urlPrefixName
    }

    private fun makePodcastDIR(): String? {
        var dir = App.inst.externalCacheDir.absolutePath + "/download/"
        val file = File(dir)
        if (file.exists()) {
            if (!file.isDirectory) {
                return null
            }
        } else {
            if (!file.mkdirs()) {
                return null
            }
        }
        return file.absolutePath
    }


}