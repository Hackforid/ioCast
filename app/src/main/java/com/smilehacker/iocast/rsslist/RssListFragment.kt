package com.smilehacker.iocast.rsslist

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import butterknife.bindView
import com.smilehacker.iocast.R
import com.smilehacker.iocast.base.mvp.MVPFragment
import com.smilehacker.iocast.model.PodcastRSS
import org.jetbrains.anko.onClick


/**
 * Created by kleist on 15/11/4.
 */
public class RssListFragment : MVPFragment<IRssListPresenter, RssListViewer>(), RssListViewer {


    val mRvItems by bindView<RecyclerView>(R.id.rv_items)
    val mBtnNew by bindView<Button>(R.id.btn_new)

    val mRssAdapter by lazy { RssAdapter(context) }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.frg_podcast_list, container, false);
        return view;
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        presenter.loadData()
    }

    fun init() {

        val gridLayoutManager = GridLayoutManager(context, 3)

        mRvItems.layoutManager = gridLayoutManager
        mRvItems.adapter = mRssAdapter

        mBtnNew.onClick {
            //presenter.addPodcast()
        }

    }

    override fun showItems(items: List<PodcastRSS>) {
        mRssAdapter.setList(items)
    }

    override fun createPresenter(): IRssListPresenter {
        return RssListPresenter()
    }

}
