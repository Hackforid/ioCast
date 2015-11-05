package com.smilehacker.iocast.frg

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import butterknife.bindView
import com.smilehacker.iocast.R
import com.smilehacker.iocast.act.AddNewPodcastActivity
import org.jetbrains.anko.support.v4.startActivity


/**
 * Created by kleist on 15/11/4.
 */
public class PodcastListFragment : Fragment() {

    val mBtnNew : Button by bindView(R.id.btn_new)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.frg_podcast_list, container, false);
        return view;
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun init() {
        mBtnNew.setOnClickListener(View.OnClickListener {
            startActivity<AddNewPodcastActivity>()
        })
    }
}
