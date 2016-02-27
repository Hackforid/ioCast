package com.smilehacker.iocast.download

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.bindView
import com.smilehacker.iocast.R
import java.util.*

/**
 * Created by kleist on 16/2/18.
 */
class DownloadAdapter : RecyclerView.Adapter<DownloadAdapter.ViewHolder>() {

    private val mFiles : MutableList<PodcastFile> = ArrayList()

    fun setFiles(files : MutableList<PodcastFile>) {
        mFiles.clear()
        mFiles.addAll(files)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder?, pos: Int) {
        val file = mFiles[pos]
        holder?.title?.text = file.item.title
    }

    override fun getItemCount(): Int {
        return mFiles.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, type : Int): ViewHolder? {
        return ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.download_frg_list_item, parent, false));
    }

    class ViewHolder : RecyclerView.ViewHolder {

        val title by bindView<TextView>(R.id.tv_title)

        constructor(itemView: View?) : super(itemView) {

        }

    }
}