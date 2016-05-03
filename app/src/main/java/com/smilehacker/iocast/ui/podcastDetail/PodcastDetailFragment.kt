package com.smilehacker.iocast.ui.podcastDetail

import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.*
import butterknife.bindView
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.request.BasePostprocessor
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.smilehacker.iocast.R
import com.smilehacker.iocast.base.mvp.MVPFragment
import com.smilehacker.iocast.model.Podcast
import com.smilehacker.iocast.model.PodcastItem
import com.smilehacker.iocast.util.DLog

/**
 * Created by kleist on 15/11/6.
 */
class PodcastDetailFragment : MVPFragment<PodcastDetailPresenter, PodcastDetailViewer>(), PodcastDetailViewer, PodcastItemAdapter.PodcastItemCallback {

    override fun createPresenter(): PodcastDetailPresenter {
        return PodcastDetailPresenterImp()
    }

    private val mIvImg: SimpleDraweeView by bindView(R.id.iv_img)
    private val mRvItems: RecyclerView by bindView(R.id.rv_items)
    private val mToolbar: Toolbar by bindView(R.id.toolbar)
    private val mToolbarLayout by bindView<CollapsingToolbarLayout>(R.id.toolbarlayout)

    private var mPodcast : Podcast? = null

    private val mItemAdapter: PodcastItemAdapter by lazy { PodcastItemAdapter(context) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.initData(arguments)
        DLog.d("init data")
        setHasOptionsMenu(true);
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fpodcastdetail_frg, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActionBar()
        initList()
        initAlbum()
        presenter.showPodcast()
    }

    private fun initList() {
        mItemAdapter.setCallback(this)
        mRvItems.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mRvItems.adapter = mItemAdapter
    }

    // list callback
    override fun onDownloadClick(item: PodcastItem) {
        presenter.startDownload(item)
    }

    private fun initActionBar() {
        val act = activity as AppCompatActivity
        act.setSupportActionBar(mToolbar)
    }

    override fun showPodcast(podcast: Podcast?) {
        mPodcast = podcast
        setPrimaryColor()

        val act = activity as AppCompatActivity
        act.titleColor = getHostActivity().getColor(R.color.text_level4_color)
        if (podcast != null) {
            act.title = podcast.title
            setAlbum(podcast.image)

            DLog.d("items size = ${podcast.items?.size}")
            mItemAdapter.items = podcast.items
        } else {
            act.title = ""
        }
    }

    private fun initAlbum() {
    }

    private fun setAlbum(url : String) {
        if (TextUtils.isEmpty(url)) {
            return
        }
        val uri = Uri.parse(url)
        val request = ImageRequestBuilder.newBuilderWithSource(uri)
                        .setPostprocessor(object : BasePostprocessor() {
                            override fun process(bitmap: Bitmap?) {
                                super.process(bitmap)
                                DLog.d("process bitmap")
                                if (mPodcast?.primaryColor == -1 && bitmap != null) {
                                    getPrimaryColor(bitmap)
                                }
                            }
                        })
                        .build()
        mIvImg.controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(mIvImg.controller)
                .build()

    }

    private fun getPrimaryColor(bitmap : Bitmap) {
        DLog.d("getPrimaryColor")
        Palette.Builder(bitmap)
            .maximumColorCount(1)
            .generate { palette ->
            palette?.swatches?.get(0)?.let {
                mPodcast?.updatePrimaryColor(it.rgb)
                DLog.d("primarycolor = " + it.rgb)
                setPrimaryColor()
            }
        }
    }

    private fun setPrimaryColor() {
        if (mPodcast == null || mPodcast?.primaryColor == -1) {
            return
        }


        mToolbarLayout.contentScrim = ColorDrawable(mPodcast!!.primaryColor)

    }

    override fun updateSubscribeStatus(podcast: Podcast?) {
        activity.invalidateOptionsMenu()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_subscribe -> {
                presenter.subscribePodcast()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_podcast_detail, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        presenter.podcast?.apply {
            DLog.d("subscribed = $subscribed")
            if (subscribed) {
                menu?.findItem(R.id.action_subscribe)?.isVisible = false
            } else {
                menu?.findItem(R.id.action_subscribe)?.isVisible = true
            }
        }
    }

    override fun startDownload(podcastItemId: Long) {
    }

    override fun updateDownloadStatus() {
        mItemAdapter.notifyDataSetChanged()
    }

    override fun startDownload(item: PodcastItem) {
        presenter.startDownload(item)
    }

    override fun pauseDownload(item: PodcastItem) {
        presenter.pauseDownload(item)
    }

    override fun onPlayClick(item: PodcastItem) {
        //presenter.startPlay(item)
        PodcastItemDetailDialog(getHostActivity())
                .build(item)
                .setListener { presenter.startPlay(item) }
                .show()
    }
}
