package com.smilehacker.iocast.base.loader

import com.smilehacker.iocast.model.PlayList

/**
 * Created by kleist on 16/4/24.
 */

class PlayListLoader : Loader {

    override fun load() {
        val history = PlayList()
        history.name = "History"
        history.type = PlayList.DB.TYPE_HISTORY
        history.save()

        val continueList = PlayList()
        continueList.name = "Continue"
        continueList.type = PlayList.DB.TYPE_CONTINUE
        continueList.save()

        val currentList = PlayList()
        currentList.name = "Current"
        currentList.type = PlayList.DB.TYPE_CURRENT
        currentList.save()

        val unplayList = PlayList()
        unplayList.name = "Unplay"
        unplayList.type = PlayList.DB.TYPE_UNPLAY
        unplayList.save()
    }


}
