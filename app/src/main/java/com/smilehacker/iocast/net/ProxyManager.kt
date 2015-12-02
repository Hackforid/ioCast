package com.smilehacker.iocast.net

import android.content.Context
import com.danikula.videocache.HttpProxyCacheServer

/**
 * Created by kleist on 15/11/17.
 */
object ProxyManager {

    private lateinit var mContext : Context

    public val httpProxyServer by lazy { HttpProxyCacheServer(mContext) }

    public fun init(context : Context) {
        mContext = context.applicationContext
    }
}