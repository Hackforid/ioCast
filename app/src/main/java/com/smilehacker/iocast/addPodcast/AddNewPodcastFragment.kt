package com.smilehacker.iocast.addPodcast

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import butterknife.bindView
import com.smilehacker.iocast.Constants
import com.smilehacker.iocast.R
import com.smilehacker.iocast.act.PodcastDetailActivity
import com.smilehacker.iocast.base.mvp.MVPFragment
import com.smilehacker.iocast.model.PodcastRSS
import com.smilehacker.iocast.net.RssDownloader
import com.smilehacker.iocast.util.DLog
import com.smilehacker.iocast.util.url.prepareURL
import org.jetbrains.anko.async
import org.jetbrains.anko.onClick
import org.jetbrains.anko.uiThread

/**
 * Created by kleist on 15/11/4.
 */
public class AddNewPodcastFragment : MVPFragment<AddPodcastPresenter, AddPodcastViewer>(), AddPodcastViewer {

    val mBtnAdd : Button by bindView(R.id.btn_add)
    val mEtUrl : EditText by bindView(R.id.et_url)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.frg_new_podcast, container, false);
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun init() {
        mBtnAdd.onClick {
            val url = mEtUrl.text.toString()
            async {
                val r = RssDownloader().download(prepareURL(url))
                if (r != null) {
                    val rss = PodcastRSS.parse(r)
                    uiThread {
                        DLog.d("download rss:" + rss?.title)
                        rss?.saveWithItems()
                        val intent = Intent(activity, PodcastDetailActivity::class.java)
                        intent.putExtra(Constants.KEY_PODCAST_TYPE, Constants.PODCAST_TYPE_ID)
                        intent.putExtra(Constants.KEY_PODCAST_ID, rss?.id)
                        DLog.d("downloaded rss:" + rss?.title)
                        startActivity(intent)
                    }
                }
            }
        }

    }

    override fun createPresenter(): AddPodcastPresenter {
        return AddPodcastPresenter()
    }
}
