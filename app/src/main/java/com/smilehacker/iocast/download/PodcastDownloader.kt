package com.smilehacker.iocast.download

import com.smilehacker.iocast.App
import com.smilehacker.iocast.model.DownloadInfo
import com.smilehacker.iocast.util.DLog
import java.io.RandomAccessFile
import java.net.URLEncoder


/**
 * Created by kleist on 16/2/17.
 */
class PodcastDownloader(val downloadInfo : DownloadInfo) : Downloader(downloadInfo.url, downloadInfo.completeSize, downloadInfo.totalSize) {
    override fun publishProcess(currentSize: Long, totalSize: Long) {
        if (downloadInfo.totalSize == 0L) {
            downloadInfo.updateTotalSize(totalSize)
        }
        throw UnsupportedOperationException()
    }

    override fun getTargetFile(url: String): RandomAccessFile {
        makePodcastDIR()
        val file = App.inst.externalCacheDir.absolutePath + "/" + url.hashCode() + ".mp3"
        DLog.d("download file at " + file.toString())
        return RandomAccessFile(file, "rwd")
    }

    fun getFileName(url : String) : String {
        var urlPrefixName = URLEncoder.encode(url, "utf8")
        if (!urlPrefixName.contains('.')) {
           urlPrefixName += ".mp3"
        }
        return urlPrefixName
    }

    private fun makePodcastDIR() {

    }


}