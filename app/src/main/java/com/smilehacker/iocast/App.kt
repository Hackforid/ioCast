package com.smilehacker.iocast

import android.app.Application
import com.activeandroid.ActiveAndroid
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.backends.okhttp.OkHttpImagePipelineConfigFactory
import com.smilehacker.iocast.net.ProxyManager
import com.squareup.okhttp.OkHttpClient
import net.danlew.android.joda.JodaTimeAndroid

/**
 * Created by kleist on 15/11/9.
 */

public class App : Application() {

    override fun onCreate() {
        super.onCreate()
        init()
    }

    fun init() {
        JodaTimeAndroid.init(this)
        ActiveAndroid.initialize(this)
        initFresco()
        ProxyManager.init(this)
    }

    fun initFresco() {
        val config = OkHttpImagePipelineConfigFactory.newBuilder(this, OkHttpClient()).build()
        Fresco.initialize(this, config)
    }

}
