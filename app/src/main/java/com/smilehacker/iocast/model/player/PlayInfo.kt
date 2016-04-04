package com.smilehacker.iocast.model.player

import android.os.Parcel
import android.os.Parcelable
import com.smilehacker.iocast.model.createParcel

/**
 * Created by kleist on 16/3/27.
 */
data class PlayInfo(
        val title : String,
        val author : String,
        val avatar : String?,
        val totalTime : Long,
        val currentTime : Long,
        val status : Int
                    ) : Parcelable {
    constructor(source: Parcel): this(source.readString(), source.readString(), source.readSerializable() as String?, source.readLong(), source.readLong(), source.readInt())

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(title)
        dest?.writeString(author)
        dest?.writeSerializable(avatar)
        dest?.writeLong(totalTime)
        dest?.writeLong(currentTime)
        dest?.writeInt(status)
    }

    companion object {
        @JvmField val CREATOR = createParcel { PlayInfo(it) }
    }
}

const val PLAY_INFO_INTENT_FILTER = "play_info_intent_filter"
