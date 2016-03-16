package com.smilehacker.iocast.net.myokhttp;

import android.content.Context;

import com.facebook.imagepipeline.core.ImagePipelineConfig;

import okhttp3.OkHttpClient;

/**
 * Created by kleist on 16/2/27.
 */
public class OkHttp3ImagePipelineConfigFactory {
    public static ImagePipelineConfig.Builder newBuilder(Context context, OkHttpClient okHttpClient) {
        return ImagePipelineConfig.newBuilder(context)
                .setNetworkFetcher(new OkHttp3NetworkFetcher(okHttpClient));
    }
}
