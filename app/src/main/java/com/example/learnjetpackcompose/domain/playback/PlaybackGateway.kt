package com.example.learnjetpackcompose.domain.playback

import com.example.learnjetpackcompose.RoomDB.Entity.Song
import com.example.learnjetpackcompose.Screen.Player.RepeatMode
import com.example.learnjetpackcompose.data.model.PlaybackManager
import kotlinx.coroutines.flow.StateFlow

interface PlaybackGateway {
    val currentSong: StateFlow<Song?>
    val isPlaying: StateFlow<Boolean>
    val isShuffle: StateFlow<Boolean>
    val repeatMode: StateFlow<RepeatMode>

    fun setQueue(
        songs: List<Song>,
        startIndex: Int,
        source: PlaybackManager.QueueSource,
        id: String? = null
    )

    fun play(song: Song)
    fun togglePlayPause()
    fun stop()
    fun next()
    fun previous()
    fun toggleShuffle()
    fun cycleRepeatMode()
}


