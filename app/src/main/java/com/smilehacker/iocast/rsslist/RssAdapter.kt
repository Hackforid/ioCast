package com.smilehacker.iocast.rsslist

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


    public fun setList(list : List<PodcastRSS>) {
        mRssList.clear()
        mRssList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val rss = mRssList[position]
        holder?.img?.setImageUrl(rss.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        return ViewHolder(mLayoutInflater.inflate(R.layout.rsslist_item, parent, false));
    }

    override fun getItemCount(): Int {
        return mRssList.size
    }

    private fun calculateGridHeight() : Int {
        return 0
    }


    class ViewHolder : RecyclerView.ViewHolder {

        val img by bindView<SimpleDraweeView>(R.id.iv_img)
        val unread by bindView<TextView>(R.id.tv_unread)

        constructor(itemView: View?) : super(itemView) {

        }

    }
}