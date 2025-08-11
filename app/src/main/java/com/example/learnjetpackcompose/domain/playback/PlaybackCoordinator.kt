package com.example.learnjetpackcompose.domain.playback

import com.example.learnjetpackcompose.RoomDB.Entity.Song
import kotlinx.coroutines.flow.StateFlow

interface PlaybackCoordinator {
    enum class Mode { IDLE, PREVIEW, FULL }

    val mode: StateFlow<Mode>
    val previewCurrentSong: StateFlow<Song?>
    val previewIsPlaying: StateFlow<Boolean>

    fun startPreview(song: Song)
    fun stopPreview()
    fun pausePreview()
    fun resumePreview()
    fun togglePreview(song: Song)
    fun onFullPlaybackStarting()
    fun onFullPlaybackStopped()
}