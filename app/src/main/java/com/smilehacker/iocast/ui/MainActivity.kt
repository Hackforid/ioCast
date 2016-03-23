package com.smilehacker.iocast.ui

import android.os.Bundle
import com.smilehacker.iocast.R
import com.smilehacker.iocast.base.newFragment
import com.smilehacker.iocast.rsslist.RssListFragment
import com.smilehacker.megatron.HostActivity

/**
 * Created by kleist on 16/3/16.
 */
class MainActivity : HostActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_host)

        if (savedInstanceState == null) {
            startFragment(newFragment<RssListFragment>())
        }
    }

    override fun getContainerID(): Int {
        return R.id.host_container
    }

    override fun onHandleSaveInstanceState(savedInstanceState: Bundle?) {
    }
}