package com.smilehacker.iocast

import android.app.Application
import com.activeandroid.ActiveAndroid
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.stetho.Stetho
import com.smilehacker.iocast.net.NetEngine
import com.smilehacker.iocast.net.ProxyManager
import com.smilehacker.iocast.net.myokhttp.OkHttp3ImagePipelineConfigFactory
import net.danlew.android.joda.JodaTimeAndroid

/**
 * Created by kleist on 15/11/9.
 */

public class App : Application() {


    companion object {
        lateinit  var inst : App
    }

    override fun onCreate() {
        super.onCreate()
        inst = this
        init()
    }

    fun init() {
        Stetho.initializeWithDefaults(this);
        JodaTimeAndroid.init(this)
        ActiveAndroid.initialize(this)
        initFresco()
        ProxyManager.init(this)
    }

    fun initFresco() {
        // TODO
        // fresco will support okhttp3 later https://github.com/facebook/fresco/issues/891
        val config = OkHttp3ImagePipelineConfigFactory.newBuilder(this, NetEngine.getFrescoHttpClient()).build()
        Fresco.initialize(this, config)
    }

}
