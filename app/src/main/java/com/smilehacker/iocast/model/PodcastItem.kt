package com.smilehacker.iocast.model

import android.os.Parcel
import android.os.Parcelable
import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import com.activeandroid.query.Select
import com.smilehacker.iocast.download.Downloader
import com.smilehacker.iocast.util.RssUtils
import org.joda.time.DateTime
import java.util.*

/**
 * Created by kleist on 16/4/23.
 */
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




