package com.smilehacker.iocast.podcastDetail

import android.content.Context
import android.os.Build
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.FloatingActionButton
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.TextView
import butterknife.bindView
import com.smilehacker.iocast.AppInfo
import com.smilehacker.iocast.R
import com.smilehacker.iocast.model.PodcastItem
import com.smilehacker.iocast.util.HtmlConverter
import org.jetbrains.anko.onClick

/**
 * Created by kleist on 16/4/6.
 */
class PodcastItemDetailDialog(val ctx : Context) : BottomSheetDialog(ctx) {

    val title by bindView<TextView>(R.id.tv_title)
    val author by bindView<TextView>(R.id.tv_author)
    val webContent by bindView<WebView>(R.id.content)
    val btnPlay by bindView<FloatingActionButton>(R.id.btn_play)

    init {
        val view = LayoutInflater.from(ctx).inflate(R.layout.dialog_podcastdetail_bottom_sheet, null)
        this.setContentView(view)
        val behavior = BottomSheetBehavior.from(view.parent as View)
        val peekHeight = (AppInfo.getDisplayMetrics().heightPixels / 3f * 2).toInt()
        behavior.peekHeight = peekHeight
    }

    fun build(item : PodcastItem) : PodcastItemDetailDialog {

        title.text = item.title
        author.text = item.author

        webContent.settings.javaScriptEnabled = false
        webContent.settings.defaultTextEncodingName = "utf-8";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webContent.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
        } else {
            webContent.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        }
        val htmlConverter = HtmlConverter()
        val htmlData = htmlConverter.fillHtmlDataWithBody(item.description)
        webContent.loadDataWithBaseURL("file:///android_asset/", htmlData, "text/html", "utf-8", "about:blank")

        return this
    }

    fun setListener(onplay : ()-> Unit) : PodcastItemDetailDialog {
        btnPlay.onClick { onplay() }
        return this
    }



}