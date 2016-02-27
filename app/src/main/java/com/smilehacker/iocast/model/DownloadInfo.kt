package com.smilehacker.iocast.model

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import com.activeandroid.query.Select

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

    object DOWNLOAD_STATUS {
        const val init = 0
        const val downloading = 1
        const val pause = 2
        const val complete = 3
        const val cancel = 4
        const val delete = 5
    }

    constructor(url : String) : this() {
        this.url = url
    }

    @Column(name = DB.PODCAST_ITEM_ID)
    var podcastItemID : Long = -1

    @Column(name = DB.URL)
    var url : String = ""

    @Column(name = DB.SIZE)
    var totalSize: Long = 0

    @Column(name = DB.COMPLETE_SIZE)
    var completeSize : Long = 0

    @Column(name = DB.STATUS)
    var status = DOWNLOAD_STATUS.init

    companion object {
        fun get(url : String) : DownloadInfo {
            return Select().from(DownloadInfo::class.java)
            .where("${DB.URL} = ?", url)
            .executeSingle()
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
}