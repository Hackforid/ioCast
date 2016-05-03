package com.smilehacker.iocast.ui

import android.os.Bundle
import butterknife.bindView
import com.smilehacker.iocast.R
import com.smilehacker.iocast.base.newFragment
import com.smilehacker.iocast.ui.bottomplayer.BottomPlayerView
import com.smilehacker.iocast.ui.playui.PlayerFragment
import com.smilehacker.iocast.ui.index.RssListFragment
import com.smilehacker.megatron.HostActivity
import com.smilehacker.megatron.KitFragment
import org.jetbrains.anko.onClick

/**
 * Created by kleist on 16/3/16.
 */
class MainActivity : HostActivity() {

    private val mVBottomPlayer  by bindView<BottomPlayerView>(R.id.bottom_player)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_host)

        if (savedInstanceState == null) {
            startFragment(newFragment<RssListFragment>())
        }

        initUI()
    }

    private fun initUI() {
        mVBottomPlayer.onClick {
            startFragment(PlayerFragment(), KitFragment.SINGLETOP)
        }
    }

    override fun getContainerID(): Int {
        return R.id.host_container
    }

    override fun onHandleSaveInstanceState(savedInstanceState: Bundle?) {
    }

    override fun onResume() {
        super.onResume()
        mVBottomPlayer.onResume()
    }



    override fun onPause() {
        super.onPause()
        mVBottomPlayer.onPause()
    }


}