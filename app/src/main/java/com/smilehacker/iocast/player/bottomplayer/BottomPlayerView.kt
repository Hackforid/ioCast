package com.smilehacker.iocast.player.bottomplayer

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.bindView
import com.facebook.drawee.view.SimpleDraweeView
import com.smilehacker.iocast.R
import com.smilehacker.iocast.model.wrap.PodcastWrap
import com.smilehacker.iocast.player.PlayController
import com.smilehacker.iocast.util.DLog
import com.smilehacker.iocast.util.setImageUrl
import org.jetbrains.anko.onClick

/**
 * Created by kleist on 16/4/7.
 */

class BottomPlayerView : RelativeLayout, BottomPlayerViewer {

    private val mIvAvatar by bindView<SimpleDraweeView>(R.id.iv_avatar)
    private val mTvTitle by bindView<TextView>(R.id.tv_title)
    private val mTvAuthor by bindView<TextView>(R.id.tv_author)
    private val mBtnPlay by bindView<ImageView>(R.id.btn_play)
    private val mProgressBar by bindView<ProgressBar>(R.id.progress)

    private var mPodcast : PodcastWrap? = null

    private val mPresenter: BottomPlayerPresenter  by lazy { BottomPlayerPresenterImpl() }

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.bottom_player, this)
        initUI()
    }

    private fun initUI() {
        refreshUI()
    }

    private fun refreshUI() {
        if (mPodcast == null) {
            return
        }
        when(mPodcast!!.playState) {
            PlayController.SIMPLE_STATE_PLAYING -> {
                mBtnPlay.onClick {
                    mBtnPlay.setImageResource(R.drawable.play_circle_outline)
                    PlayController.pause() }
                mBtnPlay.setImageResource(R.drawable.pause_circle_outline)
            }
            PlayController.SIMPLE_STATE_PAUSE -> {
                mBtnPlay.onClick {
                    mBtnPlay.setImageResource(R.drawable.pause_circle_outline)
                    PlayController.play(mPodcast!!.podcastItem.id) }
                mBtnPlay.setImageResource(R.drawable.play_circle_outline)
            }
        }
    }

    override fun updateBottomPlayer(wrap: PodcastWrap) {
        DLog.d("wrap = $wrap")
        mPodcast = wrap
        mIvAvatar.setImageUrl(wrap.podcast.image, context)
        mTvTitle.text = wrap.podcastItem.title
        mTvAuthor.text = if (!TextUtils.isEmpty(wrap.podcastItem.author)) wrap.podcastItem.author else wrap.podcast.author
        if (wrap.podcastItem.duration == 0L) {
            mProgressBar.progress = 0
        } else{
            mProgressBar.progress = (wrap.podcastItem.playedTime / wrap.podcastItem.duration * 100f).toInt()
        }

        refreshUI()
    }

    override fun onResume() {
        mPresenter.attachView(this)
    }

    override fun onPause() {
        mPresenter.detachView(false)
    }


    override fun show(show: Boolean) {
        if (show) {
            this.visibility = VISIBLE
        } else {
            this.visibility = GONE
        }
    }


}
