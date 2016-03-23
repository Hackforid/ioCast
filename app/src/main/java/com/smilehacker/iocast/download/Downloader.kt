package com.smilehacker.iocast.download

import com.smilehacker.iocast.util.DLog
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.BufferedSource
import java.io.RandomAccessFile
import java.util.concurrent.TimeUnit


/**
 * Created by kleist on 16/2/17.
 */
abstract class Downloader(val url : String, var completeSize : Long = 0L, var totalSize : Long = 0L) : Thread() {

    object STATUS {
        const val BUFFER_SIZE = 1024 * 2

        const val STATUS_INIT = 0
        const val STATUS_DOWNLOADING = 1
        const val STATUS_PAUSE = 2
        const val STATUS_COMPLETE = 3
        const val STATUS_FAIL = 4

    }

    val client = OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    var status = STATUS.STATUS_INIT

    fun pause() {
        status = STATUS.STATUS_PAUSE
    }

    override fun run() {
        super.run()
        if (totalSize == 0L) {
            initDownload()
        }
        DLog.d("start download $url, total = $totalSize")
        status = STATUS.STATUS_DOWNLOADING
        download()
    }

    private fun initDownload() {
        val request = Request.Builder().url(url).get().build()
        val response = client.newCall(request).execute()
        totalSize = response.body().contentLength()
        onGetTotalFileSize(totalSize)
    }

    private fun download() {
        if (completeSize >= totalSize) {
            return
        }

        var file : RandomAccessFile? = null
        var source : BufferedSource? = null

        try {
            file = getTargetFile(url)
            if (file == null) {
                status = STATUS.STATUS_FAIL
                onFail(Exception("get file fail"))
                return
            }
            file.seek(completeSize)

            val request = Request.Builder()
                    .url(url)
                    .get()
                    .header("Range", "bytes=$completeSize-$totalSize")
                    .build()

            val response = client.newCall(request).execute()


            source = response.body().source()
            val bytes = ByteArray(STATUS.BUFFER_SIZE)

            var currentSize = completeSize
            var length : Int = 0

            while(true) {
                if (status == STATUS.STATUS_PAUSE) {
                    onPause()
                    break
                }

                length = source.read(bytes)
                DLog.d("downloading length = $length totalSize = $totalSize, currentSize=$currentSize")
                if (status != STATUS.STATUS_DOWNLOADING) {
                    if (length == -1) {
                        onFail(Exception("net error"))
                        break
                    } else{
                        onFail(Exception("i don't know error status=$status"))
                    }
                }
                file.write(bytes, 0, length)
                currentSize += length
                if (currentSize >= totalSize) {
                    DLog.d("totalSize = $totalSize, currentSize=$currentSize")
                    status = STATUS.STATUS_COMPLETE
                    onComplete()
                    break
                } else {
                    onProcess(currentSize, totalSize)
                }
            }

        } catch (e : Exception) {
            status = STATUS.STATUS_FAIL
            DLog.e(e)
            onFail(e)
        } finally {
            source?.close()
            file?.close()
        }
    }

    abstract protected fun onComplete()

    abstract protected fun onGetTotalFileSize(totalSize : Long)

    abstract protected fun getTargetFile(url : String) : RandomAccessFile?

    abstract protected fun onProcess(currentSize : Long, totalSize : Long)

    abstract protected fun onFail(e : Exception)

    abstract protected fun onPause()

}