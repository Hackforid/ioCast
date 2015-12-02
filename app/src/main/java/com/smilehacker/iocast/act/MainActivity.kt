package com.smilehacker.iocast.act

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.smilehacker.iocast.R
import com.smilehacker.iocast.rsslist.RssListFragment
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug

/**
 * Created by kleist on 15/11/4.
 */
public class MainActivity : AppCompatActivity(), AnkoLogger {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_main)
        init()
    }

    fun init() {
        debug("init")
        val f = supportFragmentManager.beginTransaction()
        f.replace(R.id.container, RssListFragment())
        f.commit()
    }
}
