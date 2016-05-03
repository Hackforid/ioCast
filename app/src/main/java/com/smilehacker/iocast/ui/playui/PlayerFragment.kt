package com.smilehacker.iocast.ui.playui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import butterknife.bindView
import com.facebook.drawee.view.SimpleDraweeView
import com.smilehacker.iocast.R
import com.smilehacker.iocast.base.mvp.MVPFragment
import com.smilehacker.iocast.model.wrap.PodcastWrap
import com.smilehacker.iocast.player.PlayController
import com.smilehacker.iocast.ui.UIController
import com.smilehacker.iocast.util.DLog
import com.smilehacker.iocast.util.setImageUrl
import org.jetbrains.anko.onClick

/**
 * Created by kleist on 16/4/6.
 */
class PlayerFragment : MVPFragment<PlayerPresenter, PlayerViewer>(), PlayerViewer {


    private val mAlbum by bindView<SimpleDraweeView>(R.id.iv_album)
    private val mTvTitle by bindView<TextView>(R.id.tv_title)
    private val mTvAuthor by bindView<TextView>(R.id.tv_author)
    private val mBtnPlay by bindView<ImageView>(R.id.btn_play)
    private val mProgressbar by bindView<SeekBar>(R.id.progress)

    private var mPodcast : PodcastWrap? = null


    override fun createPresenter(): PlayerPresenter {
        return PlayerPresenterImpl()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        UIController.showBottomPlayer(false)
        val view = inflater?.inflate(R.layout.frg_player, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        presenter.getCurrentPodcast()?.let { showPodcast(it) }
    }

    private fun initUI() {
        mProgressbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.apply { handleProgressChange(progress) }
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }
        })
    }

    override fun showPodcast(podcastWrap: PodcastWrap) {
        mPodcast = podcastWrap
        mAlbum.setImageUrl(podcastWrap.podcast.image, context)
        mTvTitle.text = podcastWrap.podcastItem.title
        mTvAuthor.text = podcastWrap.author
        refreshUI(podcastWrap)
        updateProgress(podcastWrap.podcastItem.duration, podcastWrap.podcastItem.playedTime)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        UIController.showBottomPlayer(true)
    }

    private fun refreshUI(podcastWrap: PodcastWrap) {
        if (podcastWrap.playState != PlayController.SIMPLE_STATE_PLAYING) {
            mBtnPlay.setImageResource(R.drawable.play_black)
            mBtnPlay.onClick {
                mBtnPlay.setImageResource(R.drawable.pause_black)
                presenter.play(true) }
        } else {
            mBtnPlay.setImageResource(R.drawable.pause_black)
            mBtnPlay.onClick {
                mBtnPlay.setImageResource(R.drawable.play_black)
                presenter.play(false) }
        }
    }

    override fun updateProgress(total: Long, current: Long) {
        mProgressbar.progress = (1000f * current / total).toInt()
        DLog.d("update progress=${mProgressbar.progress}  total=$total current=$current")
    }

    private fun handleProgressChange(progress : Int) {
        DLog.d("change progress = $progress")
        if (mPodcast == null) {
            return
        }
        val time  = (mPodcast!!.podcastItem.duration * progress / 1000f).toLong()
        presenter.setPos(time)
    }
}