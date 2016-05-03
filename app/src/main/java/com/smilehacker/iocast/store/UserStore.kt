package com.smilehacker.iocast.store

import android.content.Context
import com.smilehacker.iocast.App

/**
 * Created by kleist on 16/4/7.
 */
object UserStore {

    private const val KEY_LAST_PODCAST_ITEM_ID = "key_last_podcast_item_id"
    private const val KEY_FIRST_BOOT = "key_first_boot"


    private val mPref by lazy { App.inst.getSharedPreferences("DEFAULT", Context.MODE_PRIVATE) }

    /**
     * return -1 if no podcast
     */
    var lastPodcastItemId : Long = -1
        get() = mPref.getLong(KEY_LAST_PODCAST_ITEM_ID, -1)
        set(value) {
            mPref.edit().putLong(KEY_LAST_PODCAST_ITEM_ID, value).commit()
            field = value
        }

    var isFirstBoot : Boolean
        get() = mPref.getBoolean(KEY_FIRST_BOOT, true)
        set(value) = mPref.edit().putBoolean(KEY_FIRST_BOOT, value).apply()
}