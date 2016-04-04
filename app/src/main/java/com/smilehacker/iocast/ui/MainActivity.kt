package com.smilehacker.iocast.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.bindView
import com.facebook.drawee.view.SimpleDraweeView
import com.smilehacker.iocast.Constants
import com.smilehacker.iocast.R
import com.smilehacker.iocast.base.newFragment
import com.smilehacker.iocast.model.player.PLAY_INFO_INTENT_FILTER
import com.smilehacker.iocast.model.player.PlayInfo
import com.smilehacker.iocast.model.player.PlayStatus
import com.smilehacker.iocast.rsslist.RssListFragment
import com.smilehacker.iocast.util.DLog
import com.smilehacker.iocast.util.setImageUrl
import com.smilehacker.megatron.HostActivity
import org.jetbrains.anko.ctx

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
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mPlayMsgBroadcastReceiver, IntentFilter(PLAY_INFO_INTENT_FILTER)
        )
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mPlayMsgBroadcastReceiver)
    }

    private val mPlayMsgBroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            val playInfo = intent?.getParcelableExtra<PlayInfo>(Constants.KEY_PLAY_INFO)
            playInfo?.let {
                DLog.d("play info : $it")
                mIvAvatar.setImageUrl(it.avatar, ctx)
                mTvTitle.text = it.title
                mTvAuthor.text = it.author
                mProgressBar.progress = (it.currentTime / it.totalTime * 100f).toInt()
                when(it.status) {
                    PlayStatus.PLAYING -> mBtnPlay.setImageResource(R.drawable.pause_circle_outline)
                    PlayStatus.PAUSE -> mBtnPlay.setImageResource(R.drawable.play_circle_outline)
                }
            }
        }

    }
}