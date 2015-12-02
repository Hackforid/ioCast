package com.smilehacker.iocast.util.db

import com.activeandroid.serializer.TypeSerializer
import org.joda.time.DateTime

/**
 * Created by kleist on 15/11/10.
 */
class DateTimeSerializer : TypeSerializer() {

    override fun getDeserializedType(): Class<*>? {
        return DateTime::class.java
    }

    override fun getSerializedType(): Class<*>? {
        return Long::class.java
    }

    override fun serialize(data: Any?): Any? {
        if (data == null) {
            return null
        } else {
            return (data as DateTime).millis
        }
    }

    override fun deserialize(data: Any?): Any? {
        if (data == null) {
            return null
        } else{
            return DateTime(data as Long)
        }
    }

}