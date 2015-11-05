package com.smilehacker.iocast.frg

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import butterknife.bindView
import com.smilehacker.iocast.R
import com.smilehacker.iocast.net.RssDownloader
import com.smilehacker.iocast.util.RssParser
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import org.jetbrains.anko.onClick
import org.jetbrains.anko.support.v4.onUiThread

/**
 * Created by kleist on 15/11/4.
 */
public class AddNewPodcastFragment : Fragment(), AnkoLogger {

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
            info { "brooooo" }
            val url = mEtUrl.text.toString()
            Thread() {
                run {
                    try {
                        val r = RssDownloader().download(url)
                        if (r != null) {
                            val rss = RssParser().parse(r)
                            if (rss != null) {
                                info { rss.title }
                            }
                        }
                        onUiThread{
                        }
                    } catch (e : Exception) {
                        error { Log.getStackTraceString(e) }
                    }
                }
            }.start()
        }
    }
}
