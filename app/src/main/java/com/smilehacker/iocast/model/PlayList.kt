package com.smilehacker.iocast.model

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table

/**
 * Created by kleist on 16/4/23.
 */

@Table(name = PlayList.DB.TABLENAME)
class PlayList : Model() {

    object DB {
        const val TABLENAME = "play_list"
        const val NAME = "name"
        const val TYPE = "type"
        const val STATUS = "status"

        const val TYPE_HISTORY = 1
        const val TYPE_CONTINUE = 2
        const val TYPE_UNPLAY = 3
        const val TYPE_CURRENT = 4
        const val TYPE_CUSTOM = 5
    }

    @Column(name = DB.NAME, unique = true)
    var name : String = "";
    @Column(name = DB.TYPE)
    var type : Int = 0;
    @Column(name = DB.STATUS)
    var status : Int = 0;
}
