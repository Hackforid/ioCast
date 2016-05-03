package com.smilehacker.iocast.model

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table

/**
 * Created by kleist on 16/4/19.
 */
@Table(name = PlayListItem.DB.TABLENAME)
class PlayListItem : Model() {

    object DB {
        const val TABLENAME = "play_list_item"
        const val PLAY_LIST = "play_list"
        const val PODCAST_ITEM = "podcast_item"
        const val ORDER = "order"
        const val TYPE = "type"
        const val STATUS = "status"
    }

    companion object {
        val STATUS_PLAYED_COMPLETE  = 1
        val STATUS_UNPLAYED = 2
        val STATUS_PLAYED_NOT_COMPLETE = 3
    }


    @Column(name = DB.PLAY_LIST, index = true)
    var playList : PlayList? = null
    @Column(name = DB.PODCAST_ITEM, index = true, unique = true)
    var podcastItem : PodcastItem? = null
    @Column(name = DB.ORDER)
    var order : Int = 0 // 排序
    @Column(name = DB.TYPE)
    var type : Int = 0
    @Column(name = DB.STATUS)
    var status : Int = STATUS_UNPLAYED
}