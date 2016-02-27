package com.smilehacker.iocast.download

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.bindView
import com.smilehacker.iocast.R
import com.smilehacker.iocast.base.mvp.MVPFragment

/**
 * Created by kleist on 16/2/18.
 */
class DownloadFragment : MVPFragment<DownloadPresenter, DownloadViewer>(), DownloadViewer {

    private val mRvDownload : RecyclerView by bindView(R.id.download_list)

    private val mDownloadAdapter : DownloadAdapter by lazy { DownloadAdapter()}

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.download_frg, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        showFileList(presenter.getFileList())
    }

    override fun createPresenter(): DownloadPresenter {
        return DownloadPresenterImp()
    }

    override fun showFileList(list: MutableList<PodcastFile>) {
        mDownloadAdapter.setFiles(list)
    }

    private fun initView() {
        mRvDownload.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mRvDownload.adapter = mDownloadAdapter
    }
}