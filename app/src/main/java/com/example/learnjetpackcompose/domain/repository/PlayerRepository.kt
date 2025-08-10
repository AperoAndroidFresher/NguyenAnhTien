package com.example.learnjetpackcompose.domain.repository

import com.example.learnjetpackcompose.RoomDB.Entity.Song
import com.example.learnjetpackcompose.Screen.Player.RepeatMode
import kotlinx.coroutines.flow.StateFlow

interface PlayerRepository {
    val currentSong: StateFlow<Song?>
    val isPlaying: StateFlow<Boolean>
    val isShuffle: StateFlow<Boolean>
    val repeatMode: StateFlow<RepeatMode>

    fun playSong(song: Song)
    fun togglePlayPause()
    fun stopPlayback()
    fun skipToNext()
    fun skipToPrevious()

    fun setQueueFromLocal(songs: List<Song>, startIndex: Int)
    fun setQueueFromRemote(songs: List<Song>, startIndex: Int, queryId: String? = null)
    fun setQueueFromPlaylist(playlistId: String, songs: List<Song>, startIndex: Int)
    fun toggleShuffle()
    fun cycleRepeatMode()
}