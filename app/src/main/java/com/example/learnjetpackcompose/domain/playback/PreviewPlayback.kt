package com.example.learnjetpackcompose.domain.playback

interface PreviewPlayback {
    fun play(data: String)
    fun pause()
    fun resume()
    fun stop()
    fun isPlaying(): Boolean
}