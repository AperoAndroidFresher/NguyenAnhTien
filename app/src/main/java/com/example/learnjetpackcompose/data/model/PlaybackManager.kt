package com.example.learnjetpackcompose.data.model

import com.example.learnjetpackcompose.RoomDB.Entity.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object PlaybackManager {
    private val _currentSong = MutableStateFlow<Song?>(null)
    private val _isPlaying = MutableStateFlow(false)

    val currentSong = _currentSong.asStateFlow()
    val isPlaying = _isPlaying.asStateFlow()

    fun setNowPlaying(song: Song?) {
        _currentSong.value = song
    }

    fun setIsPlaying(playing: Boolean) {
        _isPlaying.value = playing
    }
}


