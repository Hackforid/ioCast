package com.smilehacker.iocast.model

import android.os.Parcel
import android.os.Parcelable
import com.activeandroid.ActiveAndroid
import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import com.activeandroid.query.Select
import com.smilehacker.iocast.download.Downloader
import com.smilehacker.iocast.util.RssParser
import com.smilehacker.iocast.util.RssUtils
import org.joda.time.DateTime
import java.util.*

/**
 * Created by kleist on 15/11/5.
 */
@Table(name = Podcast.DB.TABLENAME)
class Podcast() : Model(), Parcelable {

    object DB {
        const val TABLENAME = "podcast"
        const val TITLE = "title"
        const val LINK = "link"
        const val DESC = "description"
        const val LANG = "language"
        const val IMAGE = "image"
        const val AUTHOR = "author"
        const val CATEGORY = "category"
        const val SUBSCRIBE = "subscribe"
        const val FEED_URL = "feed_url"
        const val PRIMARY_COLOR = "primary_color"
    }

    @Column(name = DB.TITLE)
    var title : String = ""
    @Column(name = DB.LINK)
    var link : String = ""
    @Column(name = DB.DESC)
    var description : String = ""
    @Column(name = DB.LANG)
    var language : String = ""
    @Column(name = DB.IMAGE)
    var image : String = ""
    @Column(name = DB.AUTHOR)
    var author : String = ""
    @Column(name = DB.CATEGORY)
    var category : String = ""
    @Column(name = DB.SUBSCRIBE)
    var subscribed: Boolean = false
    @Column(name = DB.FEED_URL, index = true, unique = true)
    var feedUrl : String = ""
    @Column(name = DB.PRIMARY_COLOR)
    var primaryColor  : Int = -1

    var items : ArrayList<PodcastItem>? = null

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.title)
        dest.writeString(this.link)
        dest.writeString(this.description)
        dest.writeString(this.language)
        dest.writeString(this.image)
        dest.writeString(this.author)
        dest.writeString(this.category)
        dest.writeString(this.feedUrl)
        dest.writeTypedList<PodcastItem>(this.items)
        dest.writeInt(this.primaryColor)

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
        this.feedUrl = parcel.readString()
        this.items = parcel.createTypedArrayList(PodcastItem.CREATOR)
        this.primaryColor = parcel.readInt()
    }

    companion object {

        // for parcelable
        @JvmField val CREATOR = createParcel { Podcast(it) }

        // static method
        fun parse(rssXml : String) : Podcast? {
            return RssParser().parse(rssXml)
        }

        // DB function
        fun get(id : Long) : Podcast? {
            val podcast : Podcast? = Select().from(Podcast::class.java)
                    .where("Id=?", id)
                    .executeSingle()
            return podcast
        }

        fun getByFeedUrl(url : String) : Podcast? {
            return Select().from(Podcast::class.java)
                    .where("${DB.FEED_URL} = ?", url)
                    .executeSingle()
        }

        fun getByItemIds(ids : MutableList<Long>) : MutableList<Podcast> {
            val strIds = ids.joinToString(",", "(", ")")
            val podcasts = Select().from(Podcast::class.java)
                    .where("Id IN $strIds")
                    .execute<Podcast>()
            return podcasts
        }

        fun getWithItems(id : Long) : Podcast? {
            val podcast : Podcast = Select().from(Podcast::class.java)
                    .where("Id=?", id)
                    .executeSingle()
            podcast?.items = PodcastItem.getByPodcastID(id)
            return podcast
        }

        fun getAll(subscribed : Boolean? = null) : List<Podcast> {
            if (subscribed != null) {
                return Select().from(Podcast::class.java)
                        .where("${DB.SUBSCRIBE} = ?", 1)
                        .execute<Podcast>()
            } else{
                return Select().from(Podcast::class.java)
                        .execute<Podcast>()
            }
        }

    }

    fun subscribe(subscribed : Boolean = true) {
        this.subscribed = true
        this.saveWithItems()
    }

    fun update(podcast : Podcast, withItem : Boolean = true, withSubscribed : Boolean = false) {
        this.title = podcast.title
        this.link = podcast.link
        this.description = podcast.description
        this.language = podcast.language
        this.image = podcast.image
        this.author = podcast.author
        this.category = podcast.category
        if (withSubscribed) {
            this.subscribed = podcast.subscribed
        }
        this.items = podcast.items
        if (withItem) {
            this.saveWithItems()
        } else {
            this.save()
        }
    }

    fun saveWithItems() : Podcast {
        ActiveAndroid.beginTransaction()
        this.save()
        items?.let{ updateItems(it) }
        ActiveAndroid.setTransactionSuccessful()
        ActiveAndroid.endTransaction()
        return this
    }

    private fun updateItems(items : MutableList<PodcastItem>) {
        val itemsInDB = PodcastItem.getByPodcastID(this.id)
        for (i in 0..items.size - 1) {
            val item = items[i]
            val _item = itemsInDB.find { it.title.equals(item.title) }
            if (_item == null) {
                item.podcastID = id
                item.save()
            } else {
                _item.update(item)
                items[i] = _item
            }
        }


    }

    fun updatePrimaryColor(color : Int) {
        this.primaryColor = color
        save()
    }
}

@Table(name = "podcast_item")
class PodcastItem() : Model(), Parcelable {

    object DB {
        const val PODCAST_ID = "podcast_id"
        const val PLAYED_TIME = "played_time"
    }

    @Column(name = DB.PODCAST_ID, index = true)
    var podcastID : Long = 0
    @Column(name = "title", index = true, unique = true)
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
    var duration : Long = 0
    @Column(name = "file_type")
    var fileType : String = ""
    @Column(name = "file_url")
    var fileUrl : String = ""
    @Column(name = DB.PLAYED_TIME)
    var playedTime: Long = -1L

    var totalSize = 0L
    var completeSize = 0L
    var downloadStatus = Downloader.STATUS.STATUS_INIT


    fun setDownloadInfo(totalSize : Long, completeSize : Long, status : Int) {
        this.totalSize = totalSize
        this.completeSize = completeSize
        this.downloadStatus = status
    }

    fun setPubDateByRssDate(date : String) {
        this.pubData = RssUtils.getPubDate(date)
    }


    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(this.id)
        dest.writeLong(this.podcastID)
        dest.writeString(this.title)
        dest.writeString(this.description)
        dest.writeString(this.link)
        dest.writeString(this.author)
        dest.writeSerializable(pubData)
        dest.writeLong(this.duration)
        dest.writeString(this.fileType)
        dest.writeString(this.fileUrl)
        dest.writeLong(this.playedTime)

        dest.writeLong(this.totalSize)
        dest.writeLong(this.completeSize)
        dest.writeInt(this.downloadStatus)
    }

    override fun describeContents(): Int {
        return 0
    }

    protected constructor(parcel : Parcel) : this() {

        this.podcastID = parcel.readLong()
        this.title = parcel.readString()
        this.description = parcel.readString()
        this.link = parcel.readString()
        this.author = parcel.readString()
        this.pubData = parcel.readSerializable() as DateTime?
        this.duration = parcel.readLong()
        this.fileType = parcel.readString()
        this.fileUrl = parcel.readString()
        this.playedTime = parcel.readLong()

        this.totalSize = parcel.readLong()
        this.completeSize = parcel.readLong()
        this.downloadStatus = parcel.readInt()
    }

    fun saveOrUpdate() {
        throw UnknownError()
    }

    fun update(item : PodcastItem) {
        this.description = item.description
        this.link = item.link
        this.author = item.author
        this.pubData = item.pubData
        this.duration = item.duration
        this.fileType = item.fileType
        this.fileUrl = item.fileUrl
        if (item.playedTime != -1L) {
            this.playedTime = item.playedTime
        }
        this.save()
    }

    fun updatePlayedTime(time : Long) {
        this.playedTime = time
        save()
    }

    companion object {
        @JvmField val CREATOR = createParcel { PodcastItem(it) }

        fun get(id : Long) : PodcastItem? {
            return Select().from(PodcastItem::class.java)
                        .where("Id = $id")
                        .executeSingle()
        }

        fun getByPodcastID(id : Long) : ArrayList<PodcastItem> {
            return Select().from(PodcastItem::class.java)
                    .where("podcast_id=?", id).execute<PodcastItem>() as ArrayList<PodcastItem>
        }

        fun getByPodcastIDs(ids : MutableList<Long>) : MutableList<PodcastItem> {
            val strIds = ids.joinToString(",", "(", ")")
            return Select().from(PodcastItem::class.java)
                    .where("podcast_id in $strIds").execute<PodcastItem>()
        }
    }

}





