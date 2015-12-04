package com.smilehacker.iocast.model

import android.os.Parcel
import android.os.Parcelable
import com.activeandroid.ActiveAndroid
import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import com.activeandroid.query.Select
import com.smilehacker.iocast.util.RssParser
import com.smilehacker.iocast.util.RssUtils
import org.joda.time.DateTime
import java.util.*

/**
 * Created by kleist on 15/11/5.
 */
@Table(name = "podcast")
public class PodcastRSS() : Model(), Parcelable {

    @Column(name = "title")
    var title : String = ""
    @Column(name = "link", index = true)
    var link : String = ""
    @Column(name = "description")
    var description : String = ""
    @Column(name = "language")
    var language : String = ""
    @Column(name = "image")
    var image : String = ""
    @Column(name = "author")
    var author : String = ""
    @Column(name = "category")
    var category : String = ""

    var items : ArrayList<PodcastItem>? = null

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.title)
        dest.writeString(this.link)
        dest.writeString(this.description)
        dest.writeString(this.language)
        dest.writeString(this.image)
        dest.writeString(this.author)
        dest.writeString(this.category)
        dest.writeTypedList<PodcastItem>(this.items)

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
        this.items = parcel.createTypedArrayList(PodcastItem.CREATOR)
    }

    companion object {
        val CREATOR = createParcel { PodcastRSS(it) }

        fun parse(rssXml : String) : PodcastRSS? {
            return RssParser().parse(rssXml)
        }

        fun getWithItems(id : Long) : PodcastRSS? {
            val podcast = Select().from(PodcastRSS::class.java)
                    .where("Id=?", id)
                    .executeSingle<PodcastRSS>()
            podcast?.items = PodcastItem.getByPodcastID(id)
            return podcast
        }

        fun getAll() : List<PodcastRSS> {
            return Select().from(PodcastRSS::class.java)
                    .execute<PodcastRSS>()
        }
    }

    fun saveWithItems() : PodcastRSS {
        ActiveAndroid.beginTransaction()
        this.save()
        if (items != null) {
            for(item in items!!.iterator()) {
                item.podcastID = id
                item.save()
            }
        }
        ActiveAndroid.setTransactionSuccessful()
        ActiveAndroid.endTransaction()
        return this
    }
}

@Table(name = "podcast_item")
class PodcastItem() : Model(), Parcelable {

    @Column(name = "podcast_id", index = true)
    var podcastID : Long = 0
    @Column(name = "title")
    var title : String = ""
    @Column(name = "description")
    var description : String = ""
    @Column(name = "link")
    var link : String = ""
    @Column(name = "author")
    var author : String = ""
    @Column(name = "pub_date")
    var pubData : DateTime? = null
    @Column(name = "duration")
    var duration : Int = 0
    @Column(name = "file_type")
    var fileType : String = ""
    @Column(name = "file_url")
    var fileUrl : String = ""


    fun setPubDateByRssDate(date : String) {
        this.pubData = RssUtils.getPubDate(date)
    }


    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.title)
        dest.writeString(this.description)
        dest.writeString(this.link)
        dest.writeString(this.author)
        dest.writeSerializable(pubData)
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
        this.pubData = parcel.readSerializable() as DateTime?
        this.duration = parcel.readInt()
        this.fileType = parcel.readString()
        this.fileUrl = parcel.readString()
    }

    companion object {
        val CREATOR = createParcel { PodcastItem(it) }

        fun getByPodcastID(id : Long) : ArrayList<PodcastItem> {
            return Select().from(PodcastItem::class.java)
                    .where("podcast_id=?", id).execute<PodcastItem>() as ArrayList<PodcastItem>
        }
    }

}





