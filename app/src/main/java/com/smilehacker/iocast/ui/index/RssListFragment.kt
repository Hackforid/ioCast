package com.smilehacker.iocast.ui.index

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.*
import butterknife.bindView
import com.smilehacker.iocast.Constants
import com.smilehacker.iocast.R
import com.smilehacker.iocast.base.mvp.MVPFragment
import com.smilehacker.iocast.base.newFragment
import com.smilehacker.iocast.model.Podcast
import com.smilehacker.iocast.ui.newPodcast.NewPodcastFragment
import com.smilehacker.iocast.ui.podcastDetail.PodcastDetailFragment
import com.smilehacker.iocast.util.DLog
import org.jetbrains.anko.support.v4.withArguments


/**
 * Created by kleist on 15/11/4.
 */
class RssListFragment : MVPFragment<RssListPresenter, RssListViewer>(), RssListViewer {

    companion object {
    }

    val mToolbar by bindView<Toolbar>(R.id.toolbar)
    val mRvItems by bindView<RecyclerView>(R.id.rv_items)

    val mRssAdapter by lazy { RssAdapter(context) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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

    private fun init() {
        mRssAdapter.setCallback(object : RssAdapter.RssListCallback {
            override fun onItemClick(rss: Podcast) {
                jumpToPodcastView(rss.id)
            }
        })

        mRvItems.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mRvItems.adapter = mRssAdapter

        initToolbar()
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(mToolbar)
    }

    override fun showItems(items: List<Podcast>) {
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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_podcast_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.menu_add -> {
                presenter.addPodcast()
                return true
            }
            R.id.menu_download -> {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
