package com.smilehacker.iocast

import android.app.Application
import com.smilehacker.iocast.base.loader.BootLoader
import com.smilehacker.iocast.base.loader.PlayListLoader
import com.smilehacker.iocast.store.UserStore

/**
 * Created by kleist on 15/11/9.
 */

class App : Application() {


    companion object {
        lateinit  var inst : App
    }

    override fun onCreate() {
        super.onCreate()
        inst = this
        init()
    }

    private fun init() {
        processBoot()
        processFirstBoot()
    }

    private fun processBoot() {
        BootLoader().load()
    }

    private fun processFirstBoot() {
        if (UserStore.isFirstBoot) {
            UserStore.isFirstBoot = false
            PlayListLoader().load()
        }
    }


}
