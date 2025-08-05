package com.example.learnjetpackcompose.Screen.Playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnjetpackcompose.RoomDB.Entity.Playlist
import com.example.learnjetpackcompose.RoomDB.Entity.Song
import com.example.learnjetpackcompose.data.repository.IPlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val playlistRepository: IPlaylistRepository
) : ViewModel() {


    private val currentUserId: Int = 1 // Placeholder

    private val _state = MutableStateFlow(PlaylistState())
    val state = _state.asStateFlow()

    private val _effect = Channel<PlaylistEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadPlaylists()
    }

    fun processIntent(intent: PlaylistIntent) {
        when (intent) {
            is PlaylistIntent.LoadPlaylists -> {
                loadPlaylists()
            }

            is PlaylistIntent.AddPlaylist -> {
                addPlaylist(intent.title)
            }

            is PlaylistIntent.RemovePlaylist -> {
                removePlaylist(intent.playlist)
            }

            is PlaylistIntent.RenamePlaylist -> {
                renamePlaylist(intent.playlist)
            }

            is PlaylistIntent.AddSongToPlaylist -> {
                addSongToPlaylist(intent.playlistId, intent.song)
            }

            is PlaylistIntent.RemoveSongFromPlaylist -> {
                removeSongFromPlaylist(intent.playlistId, intent.song)
            }
        }
    }

    fun getPlaylistById(playlistId: Int): Playlist? {
        return _state.value.playlists.find { it.playlistId == playlistId }
    }

    private fun loadPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }
            try {
                val playlists = playlistRepository.getPlaylistsForUser(currentUserId)
                _state.update { it.copy(playlists = playlists, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = "Failed to load playlists") }
            }
        }
    }

    private fun addPlaylist(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val newPlaylist =
                    Playlist(title = title, songs = emptyList(), userId = currentUserId)

                playlistRepository.addPlaylist(newPlaylist)
                loadPlaylists()
                _effect.send(PlaylistEffect.ShowMessage("Playlist '$title' added"))
            } catch (e: Exception) {
                _state.update { it.copy(error = "Failed to add playlist") }
            }
        }
    }

    private fun removePlaylist(playlistToRemove: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                playlistRepository.deletePlaylist(playlistToRemove.playlistId)
                loadPlaylists()
                _effect.send(PlaylistEffect.ShowMessage("Playlist '${playlistToRemove.title}' removed"))

            } catch (e: Exception) {
                _state.update { it.copy(error = "Failed to remove playlist") }
            }
        }
    }

    private fun updatePlaylist(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                playlistRepository.addPlaylist(playlist)
                loadPlaylists()
            } catch (e: Exception) {
                _state.update { it.copy(error = "Failed to update playlist") }
            }
        }
    }

    private fun renamePlaylist(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentPlaylists = _state.value.playlists
                val updatedPlaylists = currentPlaylists.map {
                    if (it.playlistId == playlist.playlistId) {
                        it.copy(title = playlist.title)
                    } else {
                        it
                    }
                }
                _state.update { it.copy(playlists = updatedPlaylists) }
                loadPlaylists()
                _effect.send(PlaylistEffect.ShowMessage("Playlist renamed to '${playlist.title}'"))
            } catch (e: Exception) {
                _state.update { it.copy(error = "Failed to rename playlist") }
            }
        }
    }

    private fun addSongToPlaylist(playlistId: Int, song: Song) {
        val playlist = _state.value.playlists.find { it.playlistId == playlistId }
        if (playlist != null) {
            val updatedPlaylist = playlist.copy(
                songs = playlist.songs + song
            )
            updatePlaylist(updatedPlaylist)
            viewModelScope.launch {
                _effect.send(PlaylistEffect.ShowMessage("Đã thêm bài hát vào '${playlist.title}'"))
            }
        }
    }

    private fun removeSongFromPlaylist(playlistId: Int, song: Song) {
        val playlist = _state.value.playlists.find { it.playlistId == playlistId }
        if (playlist != null) {
            val updatedPlaylist = playlist.copy(
                songs = playlist.songs - song
            )
            updatePlaylist(updatedPlaylist)
            viewModelScope.launch {
                _effect.send(PlaylistEffect.ShowMessage("Đã xóa bài hát vào '${playlist.title}'"))
            }
        }
    }
}