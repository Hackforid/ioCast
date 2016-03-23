package com.smilehacker.iocast.model

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import com.activeandroid.query.Select
import com.smilehacker.iocast.download.Downloader

/**
 * Created by kleist on 16/2/17.
 */
@Table(name = DownloadInfo.DB.TABLENAME)
class DownloadInfo() : Model() {
    object DB {
        const val TABLENAME = "download_info"
        const val PODCAST_ITEM_ID = "podcast_item_id"
        const val URL = "url"
        const val SIZE = "size"
        const val COMPLETE_SIZE = "complete_size"
        const val STATUS = "status"
    }

    constructor(url : String) : this() {
        this.url = url
    }

    @Column(name = DB.PODCAST_ITEM_ID, unique = true, index = true)
    var podcastItemID : Long = -1

    @Column(name = DB.URL)
    var url : String = ""

    @Column(name = DB.SIZE)
    var totalSize: Long = 0

    @Column(name = DB.COMPLETE_SIZE)
    var completeSize : Long = 0

    @Column(name = DB.STATUS)
    var status = Downloader.STATUS.STATUS_INIT

    var memStatus = Downloader.STATUS.STATUS_INIT

    var lastUpdateTime : Long = 0

    companion object {
        fun get(url : String) : DownloadInfo? {
            return Select().from(DownloadInfo::class.java)
            .where("${DB.URL} = ?", url)
            .executeSingle()
        }

        fun get(podcastItemId: Long) : DownloadInfo? {
            return Select().from(DownloadInfo::class.java)
                    .where("${DB.PODCAST_ITEM_ID} = ?", podcastItemId)
                    .executeSingle()
        }

        fun getByPodcastItemIds(podcastItemIds: MutableList<Long>) : MutableList<DownloadInfo> {
            val ids = podcastItemIds.joinToString(",")
            return Select().from(DownloadInfo::class.java)
                            .where("${DB.PODCAST_ITEM_ID} in ($ids)")
                            .execute()
        }

        fun getAll() : MutableList<DownloadInfo> {
            return Select().from(DownloadInfo::class.java)
                    .execute()
        }
    }

    fun updateTotalSize(totalSize : Long) {
        this.totalSize = totalSize
        save()
    }

    fun updateCompleteSize(completeSize: Long) {
        this.completeSize = completeSize
        save()
    }

    fun finishDownload() {
        this.completeSize = this.totalSize
        this.status = Downloader.STATUS.STATUS_COMPLETE
        save()
    }
}