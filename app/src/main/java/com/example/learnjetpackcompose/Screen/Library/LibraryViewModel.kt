package com.example.learnjetpackcompose.Screen.Library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnjetpackcompose.model.Song
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LibraryViewModel : ViewModel() {

    private val _state = MutableStateFlow(LibraryState())
    val state = _state.asStateFlow()

    private val _effect = Channel<LibraryEffect>()
    val effect = _effect.receiveAsFlow()

    fun processIntent(intent: LibraryIntent) {
        when (intent) {
            is LibraryIntent.LoadSongs -> {
                loadSongs(intent.songs)
            }

            is LibraryIntent.SelectSource -> {
                selectSource(intent.source)
            }

            is LibraryIntent.AddSongToPlaylist -> {
                removeSong(intent.song)
            }

            is LibraryIntent.LoadLocalSongs -> {
                loadLocalSongs()
            }

            is LibraryIntent.LoadRemoteSongs -> {
                loadRemoteSongs()
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
        viewModelScope.launch {
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

    private fun removeSong(songToRemove: Song) {
        viewModelScope.launch {
            try {
                val currentSongs = _state.value.songs
                val updatedSongs = currentSongs.filter { it != songToRemove }

                _state.update {
                    it.copy(
                        songs = updatedSongs,
                        filteredSongs = filterSongsBySource(updatedSongs, it.selectedSource)
                    )
                }

                _effect.send(LibraryEffect.ShowMessage("Song removed from library"))
            } catch (e: Exception) {
                _state.update { it.copy(error = "Failed to remove song") }
            }
        }
    }

    private fun loadLocalSongs() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true, selectedSource = LibrarySource.LOCAL) }

                // Simulate loading delay
                delay(1000)

                // Filter current songs to show only local ones
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
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true, selectedSource = LibrarySource.REMOTE) }

                // Simulate loading delay
                delay(1500)

                // Filter current songs to show only remote ones
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
        // This is a simple implementation. In a real app, you might have a property
        // in Song class to indicate if it's local or remote
        return when (source) {
            LibrarySource.LOCAL -> {
                // For demo purposes, assume songs with even indices are local
                songs.filterIndexed { index, _ -> index % 2 == 0 }
            }
            LibrarySource.REMOTE -> {
                // For demo purposes, assume songs with odd indices are remote
                songs.filterIndexed { index, _ -> index % 2 == 1 }
            }
        }
    }
}