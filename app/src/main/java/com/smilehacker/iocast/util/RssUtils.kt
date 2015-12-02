package com.smilehacker.iocast.util

import com.smilehacker.iocast.util.DLog
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.*

/**
 * Created by kleist on 15/11/10.
 */


public class RssUtils {
    companion object {
        const val RSS_PUB_DATE_FORMAT = "EEE, d MMM yyyy HH:mm:ss Z"

        fun getPubDate(dateStr : String) : DateTime? {
            val format = DateTimeFormat.forPattern(RSS_PUB_DATE_FORMAT).withLocale(Locale.ENGLISH)
            var time : DateTime? = null
            try {
                time = DateTime.parse(dateStr, format)
            } catch(e : Exception) {
                DLog.e(e)
            }
            return time
        }
    }
}
