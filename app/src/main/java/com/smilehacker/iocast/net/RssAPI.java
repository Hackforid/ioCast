package com.smilehacker.iocast.net;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by kleist on 15/11/5.
 */
public interface RssAPI {

    @GET("{url}")
    Call<String> getRssXml(@Path("url") String url);
}
