package com.smilehacker.iocast.rsslist

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.bindView
import com.facebook.drawee.view.SimpleDraweeView
import com.smilehacker.iocast.R
import com.smilehacker.iocast.model.PodcastRSS
import com.smilehacker.iocast.util.setImageUrl
import java.util.*

/**
 * Created by kleist on 15/11/30.
 */

class RssAdapter(val ctx : Context) : RecyclerView.Adapter<RssAdapter.ViewHolder>() {

    private val mRssList = ArrayList<PodcastRSS>()
    private val mLayoutInflater : LayoutInflater by lazy { LayoutInflater.from(ctx) }

    private  lateinit var mOnItemClickListener : View.OnClickListener

    private var mCallback : RssListCallback? = null

    init {
        mOnItemClickListener  = View.OnClickListener { v ->
            val podcast = v?.getTag(R.string.key_podcast) as PodcastRSS
            mCallback?.onItemClick(podcast)
        }
    }

    fun setCallback(callback : RssListCallback) {
        mCallback = callback
    }

    fun setList(list : List<PodcastRSS>) {
        mRssList.clear()
        mRssList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val rss = mRssList[position]

        holder?.img?.setImageUrl(rss.image)

        holder?.podcast = rss
        holder?.container?.setTag(R.string.key_podcast, rss)
        holder?.container?.setOnClickListener(mOnItemClickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        return ViewHolder(mLayoutInflater.inflate(R.layout.rsslist_item, parent, false));
    }

    override fun getItemCount(): Int {
        return mRssList.size
    }

    interface RssListCallback {
        fun onItemClick(rss : PodcastRSS)
    }

    class ViewHolder : RecyclerView.ViewHolder {

        val container by bindView<RelativeLayout>(R.id.container)
        val img by bindView<SimpleDraweeView>(R.id.iv_img)
        val unread by bindView<TextView>(R.id.tv_unread)

        var podcast : PodcastRSS? = null

        constructor(itemView: View?) : super(itemView) {

        }

    }
}