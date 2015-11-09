package com.smilehacker.iocast

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

/**
 * Created by kleist on 15/11/9.
 */

public class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
    }

}
