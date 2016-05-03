package com.smilehacker.iocast.base.loader

import android.content.Context
import com.activeandroid.ActiveAndroid
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.stetho.Stetho
import com.smilehacker.iocast.App
import com.smilehacker.iocast.net.NetEngine
import com.smilehacker.iocast.net.myokhttp.OkHttp3ImagePipelineConfigFactory
import net.danlew.android.joda.JodaTimeAndroid

/**
 * Created by kleist on 16/4/24.
 */

class BootLoader : Loader {

    override fun load() {
        val ctx = App.inst
        Stetho.initializeWithDefaults(ctx);
        JodaTimeAndroid.init(ctx)
        ActiveAndroid.initialize(ctx)
        initFresco(ctx)
    }

    private fun initFresco(ctx : Context) {
        // TODO
        // fresco will support okhttp3 later https://github.com/facebook/fresco/issues/891
        val config = OkHttp3ImagePipelineConfigFactory.newBuilder(ctx, NetEngine.getFrescoHttpClient()).build()
        Fresco.initialize(ctx, config)
    }
}
