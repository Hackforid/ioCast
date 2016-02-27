package com.smilehacker.iocast.download

import com.smilehacker.iocast.model.DownloadInfo
import com.smilehacker.iocast.model.PodcastItem
import com.smilehacker.iocast.model.PodcastRSS
import java.util.*

/**
 * Created by kleist on 16/2/18.
 */
class DownloadPresenterImp : DownloadPresenter() {

    override fun getFileList(): MutableList<PodcastFile> {
        val downloadList = DownloadInfo.getAll()

        val itemIdList = LinkedList<Long>()
        downloadList.forEach {
            if (it.podcastItemID !in itemIdList) {
                itemIdList.add(it.podcastItemID)
            }
        }

        val podcastItems = PodcastItem.getByPodcastIDs(itemIdList)

        val podcastIds = LinkedList<Long>()
        podcastItems.forEach {
            if (it.podcastID !in podcastIds) {
                podcastIds.add(it.podcastID)
            }
        }
        val podcasts = PodcastRSS.getByItemIds(podcastIds)


        val fileList = ArrayList<PodcastFile>(downloadList.size)
        downloadList.forEach {
            val podcastItemId = it.podcastItemID
            val podcastItem = podcastItems.find { it.id == podcastItemId }
            val podcast = podcasts.find { it.id == podcastItem?.id }
            if (podcast != null && podcastItem != null) {
                val file = PodcastFile(podcast, podcastItem, it)
                fileList.add(file)
            }
        }

        return fileList
    }

    override fun startDownload() {
    }

    override fun pauseDownload() {
    }

    override fun deleteFile() {
    }

    override fun playFile() {
    }
}