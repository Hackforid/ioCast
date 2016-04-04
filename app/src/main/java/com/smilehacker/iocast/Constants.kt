package com.smilehacker.iocast

/**
 * Created by kleist on 15/11/9.
 */

object Constants {
    const val KEY_PODCAST = "KEY_PODCAST"
    const val KEY_PODCAST_TYPE = "KEY_PODCAST_TYPE"
    const val KEY_PODCAST_ID = "KEY_PODCAST_ID"
    const val KEY_PODCAST_URL = "KEY_PODCAST_URL"

    const val PODCAST_TYPE_URL = "key_podcast_type_url"
    const val PODCAST_TYPE_ID = "key_podcast_type_id"

    const val KEY_DOWNLOAD_URL = "key_download_url"
    const val KEY_PODCAST_ITEM_ID = "key_podcast_item_id"
    const val KEY_DOWNLOAD_SERVICE_COMMAND = "key_download_service_command"

    const val KEY_PLAY_INFO = "key_play_info"
    const val KEY_PLAY_SERVICE_COMMAND = "key_download_service_command"
    const val KEY_PLAY_URL = "key_download_url"
    const val KEY_PLAY_PODCAST_ITEM = "key_podcast_item"

    object PODCAST_TYPE {
        const val ID = 0
        const val MEM = 1
        const val URL = 2
        const val NEWEST_MEM = 3
    }

    const val ACTION_PLAYER_NOTIFICATION = "${Config.PACKAGE_NAME}.action_player_notification"
}
