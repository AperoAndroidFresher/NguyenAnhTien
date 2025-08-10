package com.example.learnjetpackcompose.Screen.Player

import androidx.lifecycle.ViewModel
import com.example.learnjetpackcompose.RoomDB.Entity.Song
import com.example.learnjetpackcompose.domain.repository.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playerRepository: PlayerRepository
) : ViewModel() {

    val currentSong = playerRepository.currentSong
    val isPlaying: StateFlow<Boolean> = playerRepository.isPlaying
    val isShuffle: StateFlow<Boolean> = playerRepository.isShuffle
    val repeatMode: StateFlow<RepeatMode> = playerRepository.repeatMode

    fun togglePlayPause() = playerRepository.togglePlayPause()

    fun stopPlayback() = playerRepository.stopPlayback()

    fun skipToNext() = playerRepository.skipToNext()

    fun skipToPrevious() = playerRepository.skipToPrevious()

    fun setQueueFromLocal(songs: List<Song>, startIndex: Int) = playerRepository.setQueueFromLocal(songs, startIndex)

    fun setQueueFromRemote(songs: List<Song>, startIndex: Int, queryId: String? = null) =
        playerRepository.setQueueFromRemote(songs, startIndex, queryId)

    fun setQueueFromPlaylist(playlistId: String, songs: List<Song>, startIndex: Int) =
        playerRepository.setQueueFromPlaylist(playlistId, songs, startIndex)

    fun toggleShuffle() = playerRepository.toggleShuffle()

    fun cycleRepeatMode() = playerRepository.cycleRepeatMode()

    fun playSong(song: Song) = playerRepository.playSong(song)
}