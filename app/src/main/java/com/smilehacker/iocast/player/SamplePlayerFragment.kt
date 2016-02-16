package com.smilehacker.iocast.player

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import butterknife.bindView
import com.smilehacker.iocast.R
import com.smilehacker.iocast.util.DLog
import org.jetbrains.anko.onClick

/**
 * Created by kleist on 15/11/15.
 */
class SamplePlayerFragment : Fragment() {

    private val mBtnStart by bindView<ImageView>(R.id.iv_play)
    private val mSeekBar by bindView<SeekBar>(R.id.progress)
    private val mBtnForward by bindView<ImageView>(R.id.iv_forward)
    private val mBtnBack by bindView<ImageView>(R.id.iv_replay)

    private lateinit var mPlayer : PodcastPlayer
    private var mDragging = false

    private var mIsPlaying = false

    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            var pos = 0
            when(msg?.what) {
                MSG_SHOW_PROGRESS -> {
                    DLog.d("get msg")
                    pos = showProgress()
                    if (!mDragging && mPlayer.isPlaying()) {
                        val _msg = obtainMessage(MSG_SHOW_PROGRESS)
                        sendMessageDelayed(_msg, 1000L - (pos % 1000))
                    }
                }
            }

        }
    }

    companion object {
        const val MSG_SHOW_PROGRESS = 1
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.frg_sample_player, container, false);
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        mPlayer = PodcastPlayer(context)
        mSeekBar.max = 1000
        mSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(!fromUser) {
                    return
                }
                val duration = mPlayer.getDuration()
                val newPos = duration / 1000f * progress
                mPlayer.seekTo(newPos.toLong())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                mDragging = true
                mHandler.removeMessages(MSG_SHOW_PROGRESS)

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                mDragging = false
                showProgress()
                mHandler.sendEmptyMessage(MSG_SHOW_PROGRESS)
            }

        });

        mBtnStart.onClick {
            if (mIsPlaying) {
                mPlayer.pause()
                mIsPlaying = false
            } else {
                mPlayer.start()
                mIsPlaying = true
                mHandler.sendEmptyMessage(MSG_SHOW_PROGRESS)
            }
        }
    }

    private fun showProgress() : Int {
        if (mDragging) {
            return 0
        }
        val pos = mPlayer.getCurrentPostion().toInt()
        val duration = mPlayer.getDuration().toInt()
        val bufferedPos = mPlayer.getBufferedPosition().toInt()

        //DLog.d("pos=" + pos + " duration=" + duration + " bufferedPos=" + bufferedPos)

        mSeekBar.progress = if (duration == 0) 0 else (pos * 1000f / duration).toInt()
        mSeekBar.secondaryProgress = if (duration == 0) 0 else (bufferedPos * 1000f / duration).toInt()
        DLog.d("progress =" +  (pos * 1000f / duration)  + " se=" + mSeekBar.secondaryProgress)

        return pos
    }

}