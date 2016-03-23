package com.smilehacker.iocast.player

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * Created by kleist on 16/3/23.
 */
class PlayService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException()
    }
}