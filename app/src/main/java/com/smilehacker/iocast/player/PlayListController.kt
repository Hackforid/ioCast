package com.smilehacker.iocast.player

import com.activeandroid.ActiveAndroid
import com.activeandroid.query.Select
import com.smilehacker.iocast.model.PlayList
import com.smilehacker.iocast.model.PlayListItem
import com.smilehacker.iocast.model.PodcastItem
import com.smilehacker.iocast.util.DLog

/**
 * Created by kleist on 16/4/19.
 */
object PlayListController {

    lateinit var continueList : PlayList
    lateinit var historyList : PlayList
    lateinit var unplayList : PlayList

    init {
        continueList = Select().from(PlayList::class.java)
                .where("${PlayList.DB.TYPE} = ${PlayList.DB.TYPE_CONTINUE}").executeSingle()
        historyList = Select().from(PlayList::class.java)
                .where("${PlayList.DB.TYPE} = ${PlayList.DB.TYPE_HISTORY}").executeSingle()
        unplayList = Select().from(PlayList::class.java)
                .where("${PlayList.DB.TYPE} = ${PlayList.DB.TYPE_UNPLAY}").executeSingle()
    }

    fun addToContinueList(podcastItemId : Long) {
        ActiveAndroid.beginTransaction()
        try {
            val items = getPlayListItems(PlayList.DB.TYPE_CONTINUE)
            items.forEach { it.order++; it.save() }

            val item = PlayListItem()
            item.playList = continueList
            item.podcastItem = PodcastItem.get(podcastItemId)
            item.order = 0
            item.save()

            ActiveAndroid.setTransactionSuccessful()
        } catch (e : Exception) {
            DLog.e(e)
        }
        ActiveAndroid.endTransaction()
    }

    fun deleteFromContinueList(podcastItemId: Long) {
        ActiveAndroid.beginTransaction()
        try {
            val items = getPlayListItems(PlayList.DB.TYPE_CONTINUE)
            items.forEach { it.order++; it.save() }

            val item = PlayListItem()
            item.playList = continueList
            item.podcastItem = PodcastItem.get(podcastItemId)
            item.order = 0
            item.save()

            ActiveAndroid.setTransactionSuccessful()
        } catch (e : Exception) {
            DLog.e(e)
        }
        ActiveAndroid.endTransaction()
    }

    fun getPlayListTailOrder() : Long? {
        val item = Select().from(PlayListItem::class.java)
            .where("${PlayListItem.DB.STATUS} != ${PlayListItem.STATUS_PLAYED_COMPLETE}")
            .executeSingle<PlayListItem>()
        return item.id
    }

    fun getPlayList(type : Int) : PlayList {
        return when(type) {
            PlayList.DB.TYPE_CONTINUE -> continueList
            PlayList.DB.TYPE_HISTORY -> historyList
            PlayList.DB.TYPE_UNPLAY -> unplayList
            else -> {
                continueList
            }
        }
    }

    fun getPlayListItems(type : Int) : MutableList<PlayListItem> {
        val  playList = getPlayList(type)
        return Select().from(PlayListItem::class.java)
                .where("${PlayListItem.DB.PLAY_LIST} == ${playList.id}")
                .execute()
    }


}