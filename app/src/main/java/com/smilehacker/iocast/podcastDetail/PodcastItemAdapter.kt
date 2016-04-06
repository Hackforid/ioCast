package com.smilehacker.iocast.podcastDetail

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import butterknife.bindView
import com.smilehacker.iocast.R
import com.smilehacker.iocast.download.Downloader
import com.smilehacker.iocast.model.PodcastItem
import com.smilehacker.iocast.util.TimeUtils
import java.util.*

/**
 * Created by kleist on 15/11/11.
 */
class PodcastItemAdapter(val ctx : Context) : RecyclerView.Adapter<PodcastItemAdapter.ViewHolder>() {

    private val mItems : ArrayList<PodcastItem> by lazy { ArrayList<PodcastItem>() }

    private val mLayoutInflater : LayoutInflater by lazy { LayoutInflater.from(ctx)}
    private var mCallback : PodcastItemCallback? = null

    var items : ArrayList<PodcastItem>?
                get() = mItems
                set(value) {
                    mItems.clear()
                    mItems.addAll(value.orEmpty())
                    notifyDataSetChanged()
                }

    private val mDownloadClickListener = View.OnClickListener { view ->
        val item = view.getTag(R.string.tag_key_podcast_item) as PodcastItem
        if (item.downloadStatus == Downloader.STATUS.STATUS_DOWNLOADING) {
            mCallback?.pauseDownload(item)
        } else {
            mCallback?.startDownload(item)
        }
    }

    private val mPlayListener = View.OnClickListener {
        view ->
            val item = view.getTag(R.string.tag_key_podcast_item) as PodcastItem
            mCallback?.onPlayClick(item)
    }

    fun setCallback(callback : PodcastItemCallback) {
        mCallback = callback
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        return ViewHolder(mLayoutInflater.inflate(R.layout.podcastdetail_podcast_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val item = mItems[position]
        holder?.title?.text = item.title
        if (item.pubData != null) {
            holder?.pubDate?.text = item.pubData?.toString("M月d日")
        } else {
            holder?.pubDate?.text = ""
        }
        holder?.duration?.text = TimeUtils.secondToReadableString(item.duration)

        holder?.downloadBtn?.setTag(R.string.tag_key_podcast_item, item)
        holder?.itemView?.setTag(R.string.tag_key_podcast_item, item)

        when (item.downloadStatus) {
            Downloader.STATUS.STATUS_FAIL -> {
                holder?.downloadBtn?.text = "FAIL"
            }
            Downloader.STATUS.STATUS_COMPLETE -> holder?.downloadBtn?.text = "DONE"
            Downloader.STATUS.STATUS_DOWNLOADING -> holder?.downloadBtn?.text = "${(100f * item.completeSize / item.totalSize).toInt()}%"
            Downloader.STATUS.STATUS_PAUSE -> holder?.downloadBtn?.text = "PAUSE"
            Downloader.STATUS.STATUS_INIT -> {
                if (item.completeSize != 0L || item.totalSize > item.completeSize) {
                    holder?.downloadBtn?.text = "PAUSE"
                } else {
                    holder?.downloadBtn?.text = "DOWN"
                }
            }
            else -> {
                holder?.downloadBtn?.text = "DOWN"
            }
        }

        holder?.downloadBtn?.setOnClickListener(mDownloadClickListener)
        holder?.itemView?.setOnClickListener(mPlayListener)
    }

    class ViewHolder : RecyclerView.ViewHolder {
        val title by bindView<TextView>(R.id.tv_title)
        val duration by bindView<TextView>(R.id.tv_duration)
        val pubDate by bindView<TextView>(R.id.tv_pub_date)
        val downloadBtn by bindView<Button>(R.id.iv_download)

        constructor(view : View) : super(view) {

        }


    }

    interface PodcastItemCallback {
        fun onDownloadClick(item: PodcastItem);
        fun startDownload(item: PodcastItem)
        fun pauseDownload(item: PodcastItem)
        fun onPlayClick(item : PodcastItem)
    }
}