package com.smilehacker.iocast.cache

/**
 * Created by kleist on 16/2/27.
 */
interface Cache {
    fun put(key : String, value : String)

    fun get(key : String) : String?
}