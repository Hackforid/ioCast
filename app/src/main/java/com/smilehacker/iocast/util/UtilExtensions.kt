package com.smilehacker.iocast.util

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import com.facebook.drawee.view.SimpleDraweeView

/**
 * Created by kleist on 15/11/30.
 */

fun SimpleDraweeView.setImageUrl(url: String?) {
    var uri: Uri? = null
    if (TextUtils.isEmpty(url)) {
        this.setImageURI(null)
        return
    }
    try {
        uri = Uri.parse(url)
    } catch (e: Exception) {
        DLog.e(e)
        this.setImageURI(null)
    }
    this.setImageURI(uri)
}


fun SimpleDraweeView.setImageUrl(url: String?, ctx : Context) {
    var uri: Uri? = null
    if (TextUtils.isEmpty(url)) {
        this.setImageURI(null, ctx)
        return
    }
    try {
        uri = Uri.parse(url)
    } catch (e: Exception) {
        DLog.e(e)
        this.setImageURI(null, ctx)
    }
    this.setImageURI(uri, ctx)
}
