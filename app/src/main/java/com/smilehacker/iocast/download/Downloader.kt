package com.smilehacker.iocast.download

import com.smilehacker.iocast.util.DLog
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.RandomAccessFile
import java.util.concurrent.TimeUnit


/**
 * Created by kleist on 16/2/17.
 */
abstract class Downloader(val url : String, var completeSize : Long = 0L, var totalSize : Long = 0L) : Thread() {

    object Constants {
        const val BUFFER_SIZE = 1024 * 2

        const val STATUS_INIT = 0
        const val STATUS_DOWNLOADING = 1
        const val STATUS_PAUSE = 2

    }


    val client = OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    var status = Constants.STATUS_INIT


    override fun run() {
        super.run()
        if (totalSize == 0L) {
            initDownload()
        }
        DLog.d("start download $url, total = $totalSize")
        status = Constants.STATUS_DOWNLOADING
        download()
    }

    fun initDownload() {
        val request = Request.Builder().url(url).get().build()
        val response = client.newCall(request).execute()
        totalSize = response.body().contentLength()
    }

    fun download() {
        if (completeSize >= totalSize) {
            return
        }
        val file = getTargetFile(url)
        file.seek(completeSize)


        val request = Request.Builder()
                .url(url)
                .get()
                //.header("Range", "bytes=$completeSize-$totalSize")
                .build()

        val response = client.newCall(request).execute()


        val source = response.body().source()
        val bytes = ByteArray(Constants.BUFFER_SIZE)

        var currentSize = completeSize
        var length : Int
        while(true) {
            length = source.read(bytes)
            DLog.d("downloading length = $length")
            if (length == -1 || status != Constants.STATUS_DOWNLOADING) {
                break
            }
            file.write(bytes, 0, length)
            currentSize += length
            publishProcess(currentSize, totalSize)
        }

        source.close()
        file.close()
    }


    abstract fun getTargetFile(url : String) : RandomAccessFile

    abstract fun publishProcess(currentSize : Long, totalSize : Long)

}