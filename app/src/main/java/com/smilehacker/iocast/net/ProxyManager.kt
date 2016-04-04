package com.smilehacker.iocast.net

import android.content.Context
import com.danikula.videocache.HttpProxyCacheServer

/**
 * Created by kleist on 15/11/17.
 */
object ProxyManager {

    private lateinit var mContext : Context

    val httpProxyServer by lazy { HttpProxyCacheServer(mContext) }

    fun init(context : Context) {
        mContext = context.applicationContext
    }
}