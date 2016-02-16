package com.smilehacker.iocast.podcastDetail

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.bindView
import com.smilehacker.iocast.R
import com.smilehacker.iocast.model.PodcastItem
import java.util.*

/**
 * Created by kleist on 15/11/11.
 */
public class PodcastItemAdapter(val ctx : Context) : RecyclerView.Adapter<PodcastItemAdapter.ViewHolder>() {

    private val mItems : ArrayList<PodcastItem> by lazy { ArrayList<PodcastItem>() }
    private val mLayoutInflater : LayoutInflater by lazy { LayoutInflater.from(ctx)}

    public var items : ArrayList<PodcastItem>?
                get() = mItems
                set(value) {
                    mItems.clear()
                    mItems.addAll(value.orEmpty())
                    notifyDataSetChanged()
                }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        return ViewHolder(mLayoutInflater.inflate(R.layout.detail_podcast_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val item = mItems.get(position)
        holder?.title?.text = item.title
        holder?.duration?.text = item.duration.toString()
        holder?.pubDate?.text = item.pubData.toString()
    }

    public class ViewHolder : RecyclerView.ViewHolder {
        val title by bindView<TextView>(R.id.tv_title)
        val duration by bindView<TextView>(R.id.tv_duration)
        val pubDate by bindView<TextView>(R.id.tv_pub_date)

        constructor(view : View) : super(view) {

        }


    }
}