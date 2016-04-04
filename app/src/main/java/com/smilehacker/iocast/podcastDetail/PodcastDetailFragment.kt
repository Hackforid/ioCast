package com.smilehacker.iocast.podcastDetail

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.TextView
import butterknife.bindView
import com.facebook.drawee.view.SimpleDraweeView
import com.smilehacker.iocast.R
import com.smilehacker.iocast.base.mvp.MVPFragment
import com.smilehacker.iocast.model.PodcastItem
import com.smilehacker.iocast.model.PodcastRSS
import com.smilehacker.iocast.util.DLog

/**
 * Created by kleist on 15/11/6.
 */
class PodcastDetailFragment : MVPFragment<PodcastDetailPresenter, PodcastDetailViewer>(), PodcastDetailViewer, PodcastItemAdapter.PodcastItemCallback {

    override fun createPresenter(): PodcastDetailPresenter {
        return PodcastDetailPresenterImp()
    }

    private val mIvImg: SimpleDraweeView by bindView(R.id.iv_img)
    private val mTvTitle: TextView by bindView(R.id.tv_title)
    private val mTvAuthor: TextView by bindView(R.id.tv_author)
    private val mRvItems: RecyclerView by bindView(R.id.rv_items)
    private val mToolbar: Toolbar by bindView(R.id.toolbar)


    private val mItemAdapter: PodcastItemAdapter by lazy { PodcastItemAdapter(context) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.initData(arguments)
        DLog.d("init data")
        setHasOptionsMenu(true);
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fpodcastdetail_frg, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActionBar()
        initList()
        presenter.showPodcast()
    }

    private fun initList() {
        mItemAdapter.setCallback(this)
        mRvItems.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mRvItems.adapter = mItemAdapter
    }

    // list callback
    override fun onDownloadClick(item: PodcastItem) {
        presenter.startDownload(item)
    }

    private fun initActionBar() {
        val act = activity as AppCompatActivity
        act.setSupportActionBar(mToolbar)
    }

    override fun showPodcast(podcast: PodcastRSS?) {
        if (podcast != null) {
            mIvImg.setImageURI(Uri.parse(podcast.image), context)
            mTvTitle.text = podcast.title
            mTvAuthor.text = podcast.author

            DLog.d("items size = ${podcast.items?.size}")
            mItemAdapter.items = podcast.items
        }
    }

    override fun updateSubscribeStatus(podcast: PodcastRSS?) {
        activity.invalidateOptionsMenu()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_subscribe -> {
                presenter.subscribePodcast()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_podcast_detail, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        presenter.podcast?.apply {
            DLog.d("subscribed = $subscribed")
            if (subscribed) {
                menu?.findItem(R.id.action_subscribe)?.isVisible = false
            } else {
                menu?.findItem(R.id.action_subscribe)?.isVisible = true
            }
        }
    }

    override fun startDownload(podcastItemId: Long) {
    }

    override fun updateDownloadStatus() {
        mItemAdapter.notifyDataSetChanged()
    }

    override fun startDownload(item: PodcastItem) {
        presenter.startDownload(item)
    }

    override fun pauseDownload(item: PodcastItem) {
        presenter.pauseDownload(item)
    }

    override fun onPlayClick(item: PodcastItem) {
        presenter.startPlay(item)
    }
}
