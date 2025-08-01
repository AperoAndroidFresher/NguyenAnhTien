package com.example.learnjetpackcompose.Screen.Playlist.Song

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnjetpackcompose.model.Song
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SongViewModel: ViewModel() {

    private val _state = MutableStateFlow(SongState())
    val state = _state.asStateFlow()

    private val _effect = Channel<SongEffect>()
    val effect = _effect.receiveAsFlow()

    fun processIntent(intent: SongIntent) {
        when (intent) {
            is SongIntent.LoadSongs -> {
                _state.update { it.copy(songs = intent.songs, error = null) }
            }

            is SongIntent.ToggleView -> {
                _state.update { it.copy(isGridView = !it.isGridView) }
            }

            is SongIntent.RemoveSong -> {
                removeSong(intent.song)
            }

            is SongIntent.ReorderSongs -> {
                reorderSongs(intent.fromIndex, intent.toIndex)
            }
        }
    }

    private fun removeSong(songToRemove: Song) {
        viewModelScope.launch {
            try {
                val currentSongs = _state.value.songs
                val updatedSongs = currentSongs.filter { it != songToRemove }
                _state.update { it.copy(songs = updatedSongs) }
                _effect.send(SongEffect.ShowMessage("Song removed from playlist"))
            } catch (e: Exception) {
                _state.update { it.copy(error = "Failed to remove song") }
            }
        }
    }

    private fun reorderSongs(fromIndex: Int, toIndex: Int) {
        viewModelScope.launch {
            try {
                val currentSongs = _state.value.songs.toMutableList()
                if (fromIndex >= 0 && fromIndex < currentSongs.size &&
                    toIndex >= 0 && toIndex < currentSongs.size) {
                    val movedSong = currentSongs.removeAt(fromIndex)
                    currentSongs.add(toIndex, movedSong)
                    _state.update { it.copy(songs = currentSongs) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = "Failed to reorder songs") }
            }
        }
    }

}