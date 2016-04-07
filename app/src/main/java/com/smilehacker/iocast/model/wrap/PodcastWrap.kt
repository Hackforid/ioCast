package com.smilehacker.iocast.model.wrap

import com.smilehacker.iocast.model.Podcast
import com.smilehacker.iocast.model.PodcastItem
import com.smilehacker.iocast.player.PlayController

/**
 * Created by kleist on 16/4/4.
 */
data class PodcastWrap(val podcast: Podcast, val podcastItem: PodcastItem, var playState: Int = PlayController.SIMPLE_STATE_PAUSE) {
}