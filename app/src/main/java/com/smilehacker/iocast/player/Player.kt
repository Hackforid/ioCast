package com.smilehacker.iocast.player

import android.content.Context
import android.media.MediaCodec
import android.net.Uri
import android.os.Handler
import com.google.android.exoplayer.ExoPlaybackException
import com.google.android.exoplayer.ExoPlayer
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer
import com.google.android.exoplayer.MediaCodecTrackRenderer
import com.google.android.exoplayer.audio.AudioCapabilities
import com.google.android.exoplayer.audio.AudioTrack
import com.google.android.exoplayer.extractor.ExtractorSampleSource
import com.google.android.exoplayer.upstream.DefaultAllocator
import com.google.android.exoplayer.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer.upstream.DefaultUriDataSource
import com.smilehacker.iocast.util.DLog

/**
 * Created by kleist on 15/11/11.
 */

class Player(val ctx : Context) : ExoPlayer.Listener {

    object STATE {
        const val IDLE = ExoPlayer.STATE_IDLE
        const val PREPARING = ExoPlayer.STATE_PREPARING
        const val BUFFERING = ExoPlayer.STATE_BUFFERING
        const val READY = ExoPlayer.STATE_READY
        const val ENDED = ExoPlayer.STATE_ENDED
    }

    lateinit var mPlayer : ExoPlayer

    private var mPlayListener : ExoPlayer.Listener? = null

    init {
        init()
    }


    fun init() {
        mPlayer = ExoPlayer.Factory.newInstance(1)
    }

    fun prepare(url : String) {
        val allocator = DefaultAllocator(65536)
        val bandwidthMeter = DefaultBandwidthMeter(Handler(), null)
        val dataSource = DefaultUriDataSource(ctx, bandwidthMeter, "android")
        // TODO add buffer proxy
        //val uri = ProxyManager.httpProxyServer.getProxyUrl(url)
        val sampleSource = ExtractorSampleSource(Uri.parse(url),
                dataSource, allocator,
                65536 * 256)
        val audioRender = MediaCodecAudioTrackRenderer(sampleSource, null, true,
                Handler(),
                object : MediaCodecAudioTrackRenderer.EventListener {
                    override fun onAudioTrackWriteError(e: AudioTrack.WriteException?) {
                        DLog.e(e)
                    }

                    override fun onAudioTrackInitializationError(e: AudioTrack.InitializationException?) {
                        DLog.e(e)
                    }

                    override fun onDecoderInitializationError(e: MediaCodecTrackRenderer.DecoderInitializationException?) {
                        DLog.e(e)
                    }

                    override fun onDecoderInitialized(decoderName: String?, elapsedRealtimeMs: Long, initializationDurationMs: Long) {
                        DLog.d(decoderName)
                    }

                    override fun onCryptoError(e: MediaCodec.CryptoException?) {
                        DLog.e(e)
                    }


                }, AudioCapabilities.getCapabilities(ctx))

        mPlayer.prepare(audioRender)
        mPlayer.addListener(this)
    }

    fun start(pos : Long = 0L) {
        seekTo(pos)
        mPlayer.playWhenReady = true
    }

    fun pause() {
        mPlayer.playWhenReady = false
    }

    fun seekTo(positionMs : Long) {
        mPlayer.seekTo(positionMs)
    }

    fun getDuration(): Long {
        return if (mPlayer.duration != ExoPlayer.UNKNOWN_TIME) mPlayer.duration else 0
    }

    fun getCurrentPosition() : Long {
        return if (mPlayer.currentPosition != ExoPlayer.UNKNOWN_TIME) mPlayer.currentPosition / 1000 else 0
    }


    fun getBufferedPosition() : Long {
        return if (mPlayer.bufferedPosition != ExoPlayer.UNKNOWN_TIME) mPlayer.bufferedPosition else 0
    }

    fun isPlaying() : Boolean {
        return mPlayer.playWhenReady
    }

    override fun onPlayerError(error: ExoPlaybackException?) {
        mPlayListener?.onPlayerError(error)
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        mPlayListener?.onPlayerStateChanged(playWhenReady, playbackState)
    }

    override fun onPlayWhenReadyCommitted() {
        mPlayListener?.onPlayWhenReadyCommitted()
    }

    fun setPlayListener(listener : ExoPlayer.Listener) {
        mPlayListener = listener
    }

    fun getPlayState() : Int {
        return mPlayer.playbackState
    }

    fun stop() {
        mPlayer.stop()
    }

    fun release() {
        mPlayer.release()
    }
}
