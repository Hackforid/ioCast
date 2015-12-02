package com.smilehacker.iocast.util.url

/**
 * Created by kleist on 15/11/10.
 */
//public static String prepareURL(String url) {
//    url = StringUtils.trim(url);
//    if (url.startsWith("feed://")) {
//        if (BuildConfig.DEBUG) Log.d(TAG, "Replacing feed:// with http://");
//        return url.replaceFirst("feed://", "http://");
//    } else if (url.startsWith("pcast://")) {
//        if (BuildConfig.DEBUG) Log.d(TAG, "Removing pcast://");
//        return prepareURL(StringUtils.removeStart(url, "pcast://"));
//    } else if (url.startsWith("itpc")) {
//        if (BuildConfig.DEBUG) Log.d(TAG, "Replacing itpc:// with http://");
//        return url.replaceFirst("itpc://", "http://");
//    } else if (url.startsWith(AP_SUBSCRIBE)) {
//        if (BuildConfig.DEBUG) Log.d(TAG, "Removing antennapod-subscribe://");
//        return prepareURL(StringUtils.removeStart(url, AP_SUBSCRIBE));
//    } else if (!(url.startsWith("http://") || url.startsWith("https://"))) {
//        if (BuildConfig.DEBUG) Log.d(TAG, "Adding http:// at the beginning of the URL");
//        return "http://" + url;
//    } else {
//        return url;
//    }
//}

fun prepareURL(url : String) : String {
    when {
        url.startsWith("feed://") -> return url.replaceFirst("feed://", "http://")
        url.startsWith("pcast://") -> return prepareURL(url.removePrefix("pcast://"))
        url.startsWith("itpc") -> return url.replaceFirst("itpc://", "http://")
        !url.startsWith("http://") && !url.startsWith("https://") -> return "http://" + url
        else -> return url
    }
}


