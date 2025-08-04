package com.example.learnjetpackcompose.Screen.Library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnjetpackcompose.RoomDB.Entity.Playlist
import com.example.learnjetpackcompose.RoomDB.Entity.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class LibraryViewModel : ViewModel() {

    private val _state = MutableStateFlow(LibraryState())
    val state = _state.asStateFlow()

    private val _effect = Channel<LibraryEffect>()
    val effect = _effect.receiveAsFlow()

    // Hàm để cập nhật danh sách playlists (từ PlaylistViewModel)
    fun updatePlaylists(playlists: List<Playlist>) {
        _state.update { it.copy(playlists = playlists) }
    }

    fun processIntent(intent: LibraryIntent) {
        when (intent) {
            is LibraryIntent.LoadSongs -> {
                loadSongs(intent.songs)
            }
            is LibraryIntent.SelectSource -> {
                selectSource(intent.source)
            }
            is LibraryIntent.AddSongToPlaylist -> {
                addToPlaylist(intent.song)
            }
            is LibraryIntent.LoadLocalSongs -> {
                loadLocalSongs()
            }
            is LibraryIntent.LoadRemoteSongs -> {
                loadRemoteSongs()
            }
            is LibraryIntent.ShareSong -> {
                // Handle share song intent
            }
        }
    }

    private fun loadSongs(songs: List<Song>) {
        _state.update {
            it.copy(
                songs = songs,
                filteredSongs = filterSongsBySource(songs, it.selectedSource),
                error = null
            )
        }
    }

    private fun selectSource(source: LibrarySource) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    selectedSource = source,
                    filteredSongs = filterSongsBySource(it.songs, source)
                )
            }
            when (source) {
                LibrarySource.LOCAL -> {
                    _effect.send(LibraryEffect.ShowMessage("Showing local songs"))
                }
                LibrarySource.REMOTE -> {
                    _effect.send(LibraryEffect.ShowMessage("Showing remote songs"))
                }
            }
        }
    }

    private fun addToPlaylist(song: Song) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val playlists = _state.value.playlists
                if (playlists.isNullOrEmpty()) {
                    _effect.send(LibraryEffect.ShowDialogChoosePlaylist(song, emptyList()))
                    return@launch
                }
                _effect.send(LibraryEffect.ShowDialogChoosePlaylist(song, playlists))
            } catch (e: Exception) {
                _state.update { it.copy(error = "Failed to initiate playlist selection") }
            }
        }
    }

    private fun loadLocalSongs() {
        viewModelScope.launch(Dispatchers.IO) {

            try {

                _state.update { it.copy(isLoading = true, selectedSource = LibrarySource.LOCAL) }
                delay(1000)
                val localSongs = filterSongsBySource(_state.value.songs, LibrarySource.LOCAL)
                _state.update {
                    it.copy(
                        isLoading = false,
                        filteredSongs = localSongs,
                        error = null
                    )
                }
                _effect.send(LibraryEffect.ShowMessage("Local songs loaded"))
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to load local songs"
                    )
                }
            }
        }
    }

    private fun loadRemoteSongs() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.update { it.copy(isLoading = true, selectedSource = LibrarySource.REMOTE) }
                delay(1500)
                val remoteSongs = filterSongsBySource(_state.value.songs, LibrarySource.REMOTE)
                _state.update {
                    it.copy(
                        isLoading = false,
                        filteredSongs = remoteSongs,
                        error = null
                    )
                }
                _effect.send(LibraryEffect.ShowMessage("Remote songs loaded"))
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to load remote songs"
                    )
                }
            }
        }
    }

    private fun filterSongsBySource(songs: List<Song>, source: LibrarySource): List<Song> {
        return songs
    }
}