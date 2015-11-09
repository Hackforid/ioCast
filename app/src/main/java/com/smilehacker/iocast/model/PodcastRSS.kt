package com.smilehacker.iocast.model

import android.os.Parcel
import android.os.Parcelable
import com.smilehacker.iocast.util.RssParser
import java.util.*

/**
 * Created by kleist on 15/11/5.
 */
public data class PodcastRSS (
        var title : String = "",
        var link : String = "",
        var description : String = "",
        var language : String = "",
        var image : String= "",
        var author : String = "",
        var category : String = "",
        var items : ArrayList<PodcastRSS.Item> = ArrayList()
) : Parcelable {

        override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.title)
        dest.writeString(this.link)
        dest.writeString(this.description)
        dest.writeString(this.language)
        dest.writeString(this.image)
        dest.writeString(this.author)
        dest.writeString(this.category)
        dest.writeTypedList<PodcastRSS.Item>(this.items)

    }

    override fun describeContents(): Int {
        return 0
    }

    protected constructor(parcel: Parcel) : this() {
        this.title = parcel.readString()
        this.link = parcel.readString()
        this.description = parcel.readString()
        this.language = parcel.readString()
        this.image = parcel.readString()
        this.author = parcel.readString()
        this.category = parcel.readString()
        //this.items = parcel.createTypedArrayList(Item.CREATOR)
        parcel.readTypedList<Item>(this.items, Item.CREATOR)
    }

    companion object {
        fun parse(rssXml : String) : PodcastRSS? {
            return RssParser().parse(rssXml)
        }

        //val CREATOR = createParcel { PodcastRSS(it) }
        val CREATOR: Parcelable.Creator<PodcastRSS> = object : Parcelable.Creator<PodcastRSS> {
            override fun createFromParcel(source: Parcel): PodcastRSS{
                return PodcastRSS(source)
            }

            override fun newArray(size: Int): Array<PodcastRSS> {
                //return arrayOfNulls(size)
                return Array(size, {i -> PodcastRSS() })
            }
        }
    }

    data class Item (
            var title : String = "",
            var description : String = "",
            var link : String = "",
            var author : String = "",
            var pubData : String = "",
            var duration : Int = 0,
            var fileType : String = "",
            var fileUrl : String = ""
    ) : Parcelable {
        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeString(this.title)
            dest.writeString(this.description)
            dest.writeString(this.link)
            dest.writeString(this.author)
            dest.writeString(this.pubData)
            dest.writeInt(this.duration)
            dest.writeString(this.fileType)
            dest.writeString(this.fileUrl)
        }

        override fun describeContents(): Int {
            return 0
        }

        protected constructor(parcel : Parcel) : this() {
            this.title = parcel.readString()
            this.description = parcel.readString()
            this.link = parcel.readString()
            this.author = parcel.readString()
            this.pubData = parcel.readString()
            this.duration = parcel.readInt()
            this.fileType = parcel.readString()
            this.fileUrl = parcel.readString()
        }

        companion object {
            //val CREATOR = createParcel { Item(it) }
            val CREATOR: Parcelable.Creator<Item> = object : Parcelable.Creator<Item> {
                override fun createFromParcel(source: Parcel): Item{
                    return Item(source)
                }

                override fun newArray(size: Int): Array<Item> {
                    return Array(size, {i -> Item() })
                }
            }
        }

    }


}





