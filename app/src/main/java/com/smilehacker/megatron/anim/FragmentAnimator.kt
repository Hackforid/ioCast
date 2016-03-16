package com.smilehacker.megatron.anim

import android.support.annotation.AnimRes

/**
 * Fragment动画实体类
 * Created by YoKeyword on 16/2/4.
 */
open class FragmentAnimator {
    @AnimRes
    var enter: Int = 0
    @AnimRes
    var exit: Int = 0
    @AnimRes
    var popEnter: Int = 0
    @AnimRes
    var popExit: Int = 0

    constructor() {
    }

    constructor(enter: Int, exit: Int) {
        this.enter = enter
        this.exit = exit
    }

    constructor(enter: Int, exit: Int, popEnter: Int, popExit: Int) {
        this.enter = enter
        this.exit = exit
        this.popEnter = popEnter
        this.popExit = popExit
    }
}
