package com.smilehacker.iocast.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smilehacker.iocast.R
import com.smilehacker.iocast.base.BaseFragment

/**
 * Created by kleist on 16/4/6.
 */
class PlayerFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.frg_player, container, false)
        return view
    }
}