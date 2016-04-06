package com.smilehacker.iocast.util

/**
 * Created by kleist on 16/4/5.
 */
class TimeUtils {
    companion object {
        fun secondToReadableString(duration: Long) : String {
            if (duration <= 0L) {
                return "0"
            }


            val hour = duration / 3600
            val minute = duration % 3600 / 60
            val second =  duration % 3600 % 60

            return "$hour:$minute:$second"
        }
    }
}

