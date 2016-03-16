package com.smilehacker.iocast.net

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Created by kleist on 15/11/5.
 */
class RssDownloader {
    fun download(url : String) : String? {
        val client = NetEngine.getRssDownloadHttpClient()
        val request = Request.Builder().url(url)
                .get()
                .build()
        val response = client.newCall(request).execute()
        return response.body().string()
    }

    fun downloadReader(url : String) : BufferedReader {
        val client = OkHttpClient()
        Log.i("aaa", url)
        val request = Request.Builder().url(url)
                .get()
                .build()
        val response = client.newCall(request).execute()
        val inputStream = response.body().byteStream()
        val reader = BufferedReader(InputStreamReader(inputStream))
        return reader
    }
}

