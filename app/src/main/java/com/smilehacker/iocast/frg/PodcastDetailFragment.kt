package com.smilehacker.iocast.frg

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.bindView
import com.facebook.drawee.view.SimpleDraweeView
import com.smilehacker.iocast.Constants
import com.smilehacker.iocast.R
import com.smilehacker.iocast.model.PodcastRSS
import com.smilehacker.iocast.util.DLog

/**
 * Created by kleist on 15/11/6.
 */
class PodcastDetailFragment : Fragment() {

    private var mPodcastRss : PodcastRSS? = null

    private val mIvImg : SimpleDraweeView by bindView(R.id.iv_img)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //mPodcastRss = activity.intent.getParcelableExtra(Constants.KEY_PODCAST)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.frg_podcast_detail, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        mIvImg.setImageURI(Uri.parse(mPodcastRss?.image))
//        DLog.i(mPodcastRss?.title)
    }
}
