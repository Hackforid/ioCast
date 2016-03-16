package com.smilehacker.megatron.anim


import com.smilehacker.iocast.R

/**
 * Created by YoKeyword on 16/2/5.
 */
class DefaultHorizontalAnimator : FragmentAnimator() {

    init {
        enter = R.anim.h_fragment_enter
        exit = R.anim.h_fragment_exit
        popEnter = R.anim.h_fragment_pop_enter
        popExit = R.anim.h_fragment_pop_exit
    }
}
