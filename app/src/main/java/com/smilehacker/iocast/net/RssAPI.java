package com.smilehacker.iocast.net;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by kleist on 15/11/5.
 */
public interface RssAPI {

    @GET("{url}")
    Call<String> getRssXml(@Path("url") String url);
}
