package com.example.learnjetpackcompose.Screen.Player

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.learnjetpackcompose.data.repository.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playerRepository: PlayerRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val currentSong = playerRepository.currentSong
    val isPlaying: StateFlow<Boolean> = playerRepository.isPlaying

    /**
     * Toggle play/pause state of the current song
     */
    fun togglePlayPause() {
        playerRepository.togglePlayPause(context)
    }

    /**
     * Stop playback and clear player state
     */
    fun stopPlayback() {
        playerRepository.stopPlayback(context)
    }

    /**
     * Skip to next song
     */
    fun skipToNext() {
        playerRepository.skipToNext(context)
    }

    /**
     * Skip to previous song
     */
    fun skipToPrevious() {
        playerRepository.skipToPrevious(context)
    }
}