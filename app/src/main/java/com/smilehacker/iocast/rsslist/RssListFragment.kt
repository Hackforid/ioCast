package com.smilehacker.iocast.rsslist

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import butterknife.bindView
import com.smilehacker.iocast.Constants
import com.smilehacker.iocast.R
import com.smilehacker.iocast.base.mvp.MVPFragment
import com.smilehacker.iocast.base.newFragment
import com.smilehacker.iocast.model.PodcastRSS
import com.smilehacker.iocast.newPodcast.NewPodcastFragment
import com.smilehacker.iocast.podcastDetail.PodcastDetailFragment
import com.smilehacker.iocast.util.DLog
import org.jetbrains.anko.onClick
import org.jetbrains.anko.support.v4.withArguments


/**
 * Created by kleist on 15/11/4.
 */
class RssListFragment : MVPFragment<RssListPresenter, RssListViewer>(), RssListViewer {

    companion object {
    }

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
        DLog.d("onViewCreated")
        init()
        presenter.loadData()
    }

    override fun onShow() {
        super.onShow()
    }

    fun init() {
        mRssAdapter.setCallback(object : RssAdapter.RssListCallback {
            override fun onItemClick(rss: PodcastRSS) {
                jumpToPodcastView(rss.id)
            }
        })

        mRvItems.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mRvItems.adapter = mRssAdapter

        mBtnNew.onClick {
            presenter.addPodcast()
        }

        mBtnDownload.onClick {
        }

    }

    override fun showItems(items: List<PodcastRSS>) {
        mRssAdapter.setList(items)
    }

    override fun createPresenter(): RssListPresenter {
        return RssListPresenterImp()
    }

    override fun jumpToAddNewPodcastView() {
        startFragment(newFragment<NewPodcastFragment>())
    }

    override fun jumpToPodcastView(id: Long) {
        startFragment(PodcastDetailFragment()
                .withArguments(Constants.KEY_PODCAST_TYPE to Constants.PODCAST_TYPE.ID,
                               Constants.KEY_PODCAST_ID to id))
    }
}
