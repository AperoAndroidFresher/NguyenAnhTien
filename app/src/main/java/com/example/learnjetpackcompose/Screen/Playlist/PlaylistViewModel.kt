package com.example.learnjetpackcompose.Screen.Playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnjetpackcompose.model.Playlist
import com.example.learnjetpackcompose.model.Song
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaylistViewModel : ViewModel() {

    private val _state = MutableStateFlow(PlaylistState())
    val state = _state.asStateFlow()

    private val _effect = Channel<PlaylistEffect>()
    val effect = _effect.receiveAsFlow()

    // Callback để xử lý việc thêm playlist
    val onAddPlaylistClicked: () -> Unit = {
        // Việc hiển thị dialog sẽ được xử lý trong UI
    }

    fun processIntent(intent: PlaylistIntent) {
        when (intent) {
            is PlaylistIntent.LoadPlaylists -> {
                _state.update { it.copy(playlists = intent.playlists, error = null) }
            }

            is PlaylistIntent.AddPlaylist -> {
                addPlaylist(intent.playlist)
            }

            is PlaylistIntent.RemovePlaylist -> {
                removePlaylist(intent.playlist)
            }

            is PlaylistIntent.RenamePlaylist -> {
                renamePlaylist(intent.playlist)
            }
        }
    }

    private fun addPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            try {
                val currentPlaylists = _state.value.playlists
                val updatedPlaylists = currentPlaylists + playlist
                _state.update { it.copy(playlists = updatedPlaylists, error = null) }
                _effect.send(PlaylistEffect.ShowMessage("Playlist '${playlist.title}' added successfully"))
            } catch (e: Exception) {
                _state.update { it.copy(error = "Failed to add playlist") }
            }
        }
    }

    private fun removePlaylist(playlistToRemove: Playlist) {
        viewModelScope.launch {
            try {
                val currentPlaylists = _state.value.playlists
                val updatedPlaylist = currentPlaylists.filter { it.id != playlistToRemove.id }
                _state.update { it.copy(playlists = updatedPlaylist) }
                _effect.send(PlaylistEffect.ShowMessage("Playlist '${playlistToRemove.title}' removed"))
            } catch (e: Exception) {
                _state.update { it.copy(error = "Failed to remove playlist") }
            }
        }
    }

    private fun renamePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            try {
                val currentPlaylists = _state.value.playlists
                val updatedPlaylists = currentPlaylists.map {
                    if (it.id == playlist.id) {
                        it.copy(title = playlist.title)
                    } else {
                        it
                    }
                }
                _state.update { it.copy(playlists = updatedPlaylists) }
                _effect.send(PlaylistEffect.ShowMessage("Playlist renamed to '${playlist.title}'"))
            } catch (e: Exception) {
                _state.update { it.copy(error = "Failed to rename playlist") }
            }
        }
    }
}