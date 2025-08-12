package com.example.learnjetpackcompose.Screen.Player

import androidx.lifecycle.ViewModel
import com.example.learnjetpackcompose.RoomDB.Entity.Song
import com.example.learnjetpackcompose.domain.repository.PlayerRepository
import com.example.learnjetpackcompose.domain.playback.PlaybackCoordinator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val playbackCoordinator: PlaybackCoordinator
) : ViewModel() {

    val currentSong = playerRepository.currentSong
    val isPlaying: StateFlow<Boolean> = playerRepository.isPlaying
    val isShuffle: StateFlow<Boolean> = playerRepository.isShuffle
    val repeatMode: StateFlow<RepeatMode> = playerRepository.repeatMode
    val currentPosition: StateFlow<Long> = playerRepository.currentPosition
    val duration: StateFlow<Long> = playerRepository.duration

    fun processIntent(intent: PlayerIntent){
        when(intent){
            is PlayerIntent.Next -> skipToNext()
            is PlayerIntent.Pause -> togglePlayPause()
            is PlayerIntent.Play -> togglePlayPause()
            is PlayerIntent.Previous -> skipToPrevious()
            is PlayerIntent.Repeat -> cycleRepeatMode()
            is PlayerIntent.Shuffle -> toggleShuffle()
            is PlayerIntent.Stop -> stopPlayback()
            is PlayerIntent.Seek -> seekTo(intent.position)
        }
    }

    fun togglePlayPause() = playerRepository.togglePlayPause()

    fun stopPlayback() = playerRepository.stopPlayback()

    fun skipToNext() = playerRepository.skipToNext()

    fun skipToPrevious() = playerRepository.skipToPrevious()

    fun seekTo(position: Long) = playerRepository.seekTo(position)

    fun toggleShuffle() = playerRepository.toggleShuffle()

    fun cycleRepeatMode() = playerRepository.cycleRepeatMode()

    fun playSong(song: Song) = playerRepository.playSong(song)

    fun preparePlaylistPlayback(playlistId: String, songs: List<Song>, startIndex: Int) {
        playbackCoordinator.onFullPlaybackStarting()
        playerRepository.setQueueFromPlaylist(playlistId, songs, startIndex)
    }

    fun stopPreview() { playbackCoordinator.stopPreview() }
}