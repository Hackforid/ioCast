package com.smilehacker.iocast.rsslist

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import butterknife.bindView
import com.smilehacker.iocast.Constants
import com.smilehacker.iocast.R
import com.smilehacker.iocast.base.mvp.MVPFragment
import com.smilehacker.iocast.download.DownloadActivity
import com.smilehacker.iocast.model.PodcastRSS
import com.smilehacker.iocast.newPodcast.NewPodcastActivity
import com.smilehacker.iocast.podcastDetail.PodcastDetailActivity
import com.smilehacker.iocast.util.DLog
import org.jetbrains.anko.onClick
import org.jetbrains.anko.support.v4.startActivity


/**
 * Created by kleist on 15/11/4.
 */
public class RssListFragment : MVPFragment<RssListPresenter, RssListViewer>(), RssListViewer {


    val mRvItems by bindView<RecyclerView>(R.id.rv_items)
    val mBtnNew by bindView<Button>(R.id.btn_new)
    val mBtnDownload by bindView<Button>(R.id.btn_download)

    val mRssAdapter by lazy { RssAdapter(context) }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        DLog.d("onCreateView")
        val view = inflater?.inflate(R.layout.rsslist_frg, container, false);
        return view;
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        presenter.loadData()
    }

    fun init() {

        val gridLayoutManager = GridLayoutManager(context, 3)
        mRssAdapter.setGridHorizontalCount(3)

        mRssAdapter.setCallback(object : RssAdapter.RssListCallback {
            override fun onItemClick(rss: PodcastRSS) {
                jumpToPodcastView(rss.id)
            }
        })

        mRvItems.layoutManager = gridLayoutManager
        mRvItems.adapter = mRssAdapter

        mBtnNew.onClick {
            presenter.addPodcast()
        }

        mBtnDownload.onClick {
            startActivity<DownloadActivity>()
        }

    }

    override fun showItems(items: List<PodcastRSS>) {
        mRssAdapter.setList(items)
    }

    override fun createPresenter(): RssListPresenter {
        return RssListPresenterImp()
    }

    override fun jumpToAddNewPodcastView() {
        startActivity<NewPodcastActivity>()
    }

    override fun jumpToPodcastView(id: Long) {
        val intent = Intent(activity, PodcastDetailActivity::class.java)
        intent.putExtra(Constants.KEY_PODCAST_TYPE, Constants.PODCAST_TYPE_ID)
        intent.putExtra(Constants.KEY_PODCAST_ID, id)
        startActivity(intent)
    }
}
