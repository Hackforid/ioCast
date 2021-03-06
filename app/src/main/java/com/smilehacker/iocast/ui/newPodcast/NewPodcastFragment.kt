package com.smilehacker.iocast.ui.newPodcast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import butterknife.bindView
import com.jakewharton.rxbinding.view.RxView
import com.smilehacker.iocast.Constants
import com.smilehacker.iocast.R
import com.smilehacker.iocast.base.mvp.MVPFragment
import com.smilehacker.iocast.ui.podcastDetail.PodcastDetailFragment
import org.jetbrains.anko.support.v4.withArguments
import java.util.concurrent.TimeUnit

/**
 * Created by kleist on 15/11/4.
 */
class NewPodcastFragment : MVPFragment<NewPodcastPresenter, NewPodcastViewer>(), NewPodcastViewer{

    val mBtnAdd : Button by bindView(R.id.btn_add)
    val mEtUrl : EditText by bindView(R.id.et_url)



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.newpodcast_frg, container, false);
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun init() {
        RxView.clicks(mBtnAdd)
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe({presenter.addByUrl(mEtUrl.text.toString())})

    }

    override fun createPresenter(): NewPodcastPresenter{
        return NewPodcastPresenterImp()
    }

    override fun jumpToPodcastView() {
        startFragment(PodcastDetailFragment().withArguments(Constants.KEY_PODCAST_TYPE to Constants.PODCAST_TYPE.MEM))
    }



}
