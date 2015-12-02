package com.smilehacker.iocast.frg

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.bindView
import com.facebook.drawee.view.SimpleDraweeView
import com.smilehacker.iocast.Constants
import com.smilehacker.iocast.R
import com.smilehacker.iocast.adapter.PodcastItemAdapter
import com.smilehacker.iocast.model.PodcastRSS

/**
 * Created by kleist on 15/11/6.
 */
class PodcastDetailFragment : Fragment() {

    private val mPodcastRss : PodcastRSS by lazy {getPodcast()}

    private val mIvImg : SimpleDraweeView by bindView(R.id.iv_img)
    private val mTvTitle : TextView by bindView(R.id.tv_title)
    private val mTvAuthor : TextView by bindView(R.id.tv_author)
    private val mRvItems : RecyclerView by bindView(R.id.rv_items)

    private val mItemAdapter : PodcastItemAdapter by lazy {PodcastItemAdapter(context)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun getPodcast() : PodcastRSS {
        val podcastID = activity.intent.getLongExtra(Constants.KEY_PODCAST_ID, 0)
        val podcast = PodcastRSS.getWithItems(podcastID)
        if (podcast == null) {
            activity.finish()
            throw IllegalStateException("podcast can't be null")
        } else{
            return podcast
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.frg_podcast_detail, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        mIvImg.setImageURI(Uri.parse(mPodcastRss.image))
        mTvTitle.text = mPodcastRss.title
        mTvAuthor.text = mPodcastRss.author

        mRvItems.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mRvItems.adapter = mItemAdapter
        mItemAdapter.items = mPodcastRss.items
    }

}
