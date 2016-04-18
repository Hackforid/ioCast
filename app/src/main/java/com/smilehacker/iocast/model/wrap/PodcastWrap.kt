package com.smilehacker.iocast.model.wrap

import android.text.TextUtils
import com.smilehacker.iocast.model.Podcast
import com.smilehacker.iocast.model.PodcastItem
import com.smilehacker.iocast.player.PlayController

/**
 * Created by kleist on 16/4/4.
 */
data class PodcastWrap(val podcast: Podcast, val podcastItem: PodcastItem, var playState: Int = PlayController.SIMPLE_STATE_PAUSE) {
    var author : String = ""
        get() {
            if (!TextUtils.isEmpty(podcastItem.author)) {return podcastItem.author}
            if (!TextUtils.isEmpty(podcast.author)) {return podcast.author}
            return ""
        }
        private set
}