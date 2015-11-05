package com.smilehacker.iocast.net

import android.util.Log
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import java.io.*
import java.util.concurrent.TimeUnit

/**
 * Created by kleist on 15/11/5.
 */
class RssDownloader {
    fun download(url : String) : String? {
        val client = OkHttpClient()
        Log.i("aaa", url)
        client.setConnectTimeout(30, TimeUnit.SECONDS)
        client.setWriteTimeout(30, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);
        val request = Request.Builder().url(url)
                .get()
                .build()
        val response = client.newCall(request).execute()
        return response.body().string()
    }

    fun downloadReader(url : String) : BufferedReader {
        val client = OkHttpClient()
        Log.i("aaa", url)
        client.setConnectTimeout(30, TimeUnit.SECONDS)
        client.setWriteTimeout(30, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);
        val request = Request.Builder().url(url)
                .get()
                .build()
        val response = client.newCall(request).execute()
        val inputStream = response.body().byteStream()
        val reader = BufferedReader(InputStreamReader(inputStream))
        return reader
    }
}

