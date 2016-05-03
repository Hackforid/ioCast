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

class PodcastPlayer(val ctx : Context) {


    lateinit var mPlayer : ExoPlayer

    init {
        init()
    }


    fun init() {
        mPlayer = ExoPlayer.Factory.newInstance(1)
        val allocator = DefaultAllocator(65536)
        val bandwidthMeter = DefaultBandwidthMeter(Handler(), null)
        val dataSource = DefaultUriDataSource(ctx, bandwidthMeter, "android")
        DLog.d("cache dir = " + ctx.cacheDir.path)
        val url = "http://ipn.li/itgonglun/178/audio.mp3"
        //val url = "http://192.168.205.43:8000/%E5%BF%83%E5%81%9A%E3%81%9714.mp3"
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
        mPlayer.addListener(object : ExoPlayer.Listener {
            override fun onPlayerError(error: ExoPlaybackException?) {
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            }

            override fun onPlayWhenReadyCommitted() {
            }
        })
    }

    public fun start() {
        mPlayer.playWhenReady = true
    }

    public fun pause() {
        mPlayer.playWhenReady = false
    }

    public fun seekTo(positionMs : Long) {
        mPlayer.seekTo(positionMs)
    }

    public fun getDuration(): Long {
        return if (mPlayer.duration != ExoPlayer.UNKNOWN_TIME) mPlayer.duration else 0
    }

    public fun getCurrentPostion() : Long {
        return if (mPlayer.currentPosition != ExoPlayer.UNKNOWN_TIME) mPlayer.currentPosition else 0
    }


    public fun getBufferedPosition() : Long {
        return if (mPlayer.bufferedPosition != ExoPlayer.UNKNOWN_TIME) mPlayer.bufferedPosition else 0
    }

    public fun isPlaying() : Boolean {
        return mPlayer.playWhenReady
    }
}
