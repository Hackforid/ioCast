package com.smilehacker.iocast.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.bindView
import com.facebook.drawee.view.SimpleDraweeView
import com.smilehacker.iocast.Constants
import com.smilehacker.iocast.R
import com.smilehacker.iocast.base.newFragment
import com.smilehacker.iocast.model.PodcastItem
import com.smilehacker.iocast.model.player.PlayInfo
import com.smilehacker.iocast.model.player.PlayStatus
import com.smilehacker.iocast.player.PlayManager
import com.smilehacker.iocast.rsslist.RssListFragment
import com.smilehacker.iocast.util.DLog
import com.smilehacker.iocast.util.RxBus
import com.smilehacker.iocast.util.setImageUrl
import com.smilehacker.megatron.HostActivity
import org.jetbrains.anko.ctx
import org.jetbrains.anko.onClick
import rx.Subscription

/**
 * Created by kleist on 16/3/16.
 */
class MainActivity : HostActivity() {

    private val mVBottomPlayer  by bindView<FrameLayout>(R.id.v_player)
    private val mIvAvatar by bindView<SimpleDraweeView>(R.id.iv_avatar)
    private val mTvTitle by bindView<TextView>(R.id.tv_title)
    private val mTvAuthor by bindView<TextView>(R.id.tv_author)
    private val mBtnPlay by bindView<ImageView>(R.id.btn_play)
    private val mProgressBar by bindView<ProgressBar>(R.id.progress)

    private lateinit var mPlayerSubscription : Subscription

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_host)

        if (savedInstanceState == null) {
            startFragment(newFragment<RssListFragment>())
        }

        initUI()
    }

    private fun initUI() {
    }

    override fun getContainerID(): Int {
        return R.id.host_container
    }

    override fun onHandleSaveInstanceState(savedInstanceState: Bundle?) {
    }

    override fun onResume() {
        super.onResume()
//        LocalBroadcastManager.getInstance(this).registerReceiver(
//                mPlayMsgBroadcastReceiver, IntentFilter(PLAY_INFO_INTENT_FILTER)
//        )
        mPlayerSubscription = RxBus.toObservable(PodcastItem::class.java)
                .subscribe { updateBottomPlayer(it) }
    }

    private fun updateBottomPlayer(item : PodcastItem) {
        mVBottomPlayer.visibility = View.VISIBLE
        mIvAvatar.setImageUrl(item.image, ctx)
        mTvTitle.text = item.title
        mTvAuthor.text = item.author
        if (item.duration == 0L) {
            mProgressBar.progress = 0
        } else{
            mProgressBar.progress = (item.playedTime / item.duration * 100f).toInt()
        }
        when(item.playState) {
            PlayManager.SIMPLE_STATE_PLAYING -> {
                mBtnPlay.onClick { PlayManager.pause() }
                mBtnPlay.setImageResource(R.drawable.pause_circle_outline)
            }
            PlayManager.SIMPLE_STATE_PAUSE -> {
                mBtnPlay.onClick { PlayManager.start(item.playedTime) }
                mBtnPlay.setImageResource(R.drawable.play_circle_outline)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(mPlayMsgBroadcastReceiver)
        mPlayerSubscription.unsubscribe()
    }

    private val mPlayMsgBroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            val playInfo = intent?.getParcelableExtra<PlayInfo>(Constants.KEY_PLAY_INFO)
            playInfo?.let {
                DLog.d("play info : $it")
                mIvAvatar.setImageUrl(it.avatar, ctx)
                mTvTitle.text = it.title
                mTvAuthor.text = it.author
                if (it.totalTime == 0L) {
                    mProgressBar.progress = 0
                } else{
                    mProgressBar.progress = (it.currentTime / it.totalTime * 100f).toInt()
                }
                when(it.status) {
                    PlayStatus.PLAYING -> mBtnPlay.setImageResource(R.drawable.pause_circle_outline)
                    PlayStatus.PAUSE -> mBtnPlay.setImageResource(R.drawable.play_circle_outline)
                }
            }
        }

    }
}