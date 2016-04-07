package com.smilehacker.iocast.store

import android.content.Context
import com.smilehacker.iocast.App

/**
 * Created by kleist on 16/4/7.
 */
object UserStore {

    private const val KEY_LAST_PODCAST_ITEM_ID = "key_last_podcast_item_id"


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

}