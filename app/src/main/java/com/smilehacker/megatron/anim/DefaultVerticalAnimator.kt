package com.smilehacker.megatron.anim


import com.smilehacker.iocast.R

/**
 * Created by YoKeyword on 16/2/5.
 */
class DefaultVerticalAnimator : FragmentAnimator() {

    init {
        enter = R.anim.v_fragment_enter
        exit = R.anim.v_fragment_exit
        popEnter = R.anim.v_fragment_pop_enter
        popExit = R.anim.v_fragment_pop_exit
    }
}
