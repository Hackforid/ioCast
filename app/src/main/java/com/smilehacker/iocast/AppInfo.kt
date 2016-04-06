package com.smilehacker.iocast

import android.util.DisplayMetrics

/**
 * Created by kleist on 16/4/6.
 */
object AppInfo {

    fun getDisplayMetrics() : DisplayMetrics {
        return App.inst.resources.displayMetrics
    }

}