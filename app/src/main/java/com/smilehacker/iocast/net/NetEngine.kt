package com.smilehacker.iocast.net

import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Created by kleist on 16/2/27.
 */

class NetEngine {

    companion object {
        fun getRssDownloadHttpClient() : OkHttpClient {
            val client = OkHttpClient().newBuilder()
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addNetworkInterceptor(StethoInterceptor())
                    .build()

            return client
        }

        fun getFrescoHttpClient() : OkHttpClient {
            val client = OkHttpClient().newBuilder()
            .addNetworkInterceptor(StethoInterceptor())
            .build()

            return client
        }

    }
}
