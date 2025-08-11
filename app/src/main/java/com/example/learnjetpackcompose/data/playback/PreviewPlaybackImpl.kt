package com.example.learnjetpackcompose.data.playback

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import com.example.learnjetpackcompose.domain.playback.PreviewPlayback
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreviewPlayerImpl @Inject constructor(
    @ApplicationContext private val appContext: Context
) : PreviewPlayback {
    private var mediaPlayer: MediaPlayer? = null
    private var currentData: String? = null

    override fun play(data: String) {
        try {
            if (mediaPlayer != null && currentData == data) {
                if (mediaPlayer?.isPlaying == false) mediaPlayer?.start()
                return
            }
            stop()
            currentData = data
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                if (data.startsWith("content://")) {
                    val uri = Uri.parse(data)
                    try {
                        appContext.contentResolver.openAssetFileDescriptor(uri, "r")?.use { afd ->
                            setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                        } ?: throw IllegalArgumentException("Unable to open content URI")
                    } catch (_: Exception) {
                        setDataSource(appContext, uri)
                    }
                } else if (data.startsWith("file://") || data.startsWith("http")) {
                    setDataSource(appContext, Uri.parse(data))
                } else {
                    setDataSource(data)
                }
                setOnPreparedListener { it.start() }
                setOnCompletionListener { stop() }
                setOnErrorListener { _, _, _ ->
                    stop(); true
                }
                prepareAsync()
            }
        } catch (_: Exception) {
            stop()
        }
    }

    override fun pause() {
        try { mediaPlayer?.pause()
        } catch (_: Exception) {
        }
    }
    override fun resume() {
        try {
            if (mediaPlayer?.isPlaying == false) mediaPlayer?.start()
        } catch (_: Exception) {
        }
    }
    override fun stop() {
        try { mediaPlayer?.stop() } catch (_: Exception) {}
        try { mediaPlayer?.release() } catch (_: Exception) {}
        mediaPlayer = null
        currentData = null
    }
    override fun isPlaying(): Boolean = mediaPlayer?.isPlaying == true
}


