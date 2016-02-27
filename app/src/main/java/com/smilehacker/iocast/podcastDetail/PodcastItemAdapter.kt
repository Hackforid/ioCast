package com.smilehacker.iocast.podcastDetail

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.bindView
import com.smilehacker.iocast.R
import com.smilehacker.iocast.model.PodcastItem
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
        val itemUrl = view.getTag(R.string.tag_key_podcast_item_url) as String
        mCallback?.onDownloadClick(itemUrl)
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
        holder?.duration?.text = item.duration.toString()
        holder?.pubDate?.text = item.pubData.toString()

        holder?.downloadIcon?.setTag(R.string.tag_key_podcast_item_url, item.fileUrl)
        holder?.downloadIcon?.setOnClickListener(mDownloadClickListener)
    }

    class ViewHolder : RecyclerView.ViewHolder {
        val title by bindView<TextView>(R.id.tv_title)
        val duration by bindView<TextView>(R.id.tv_duration)
        val pubDate by bindView<TextView>(R.id.tv_pub_date)
        val downloadIcon by bindView<ImageView>(R.id.iv_download)

        constructor(view : View) : super(view) {

        }


    }

    interface PodcastItemCallback {
        fun onDownloadClick(itemUrl: String);
    }
}