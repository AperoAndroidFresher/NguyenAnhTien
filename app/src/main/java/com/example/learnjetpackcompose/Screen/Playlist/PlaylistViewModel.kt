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


    val onAddPlaylistClicked: () -> Unit = {

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
            is PlaylistIntent.AddSongToPlaylist -> {
                addSongToPlaylist(intent.playlistId, intent.song)
            }
            is PlaylistIntent.RemoveSongFromPlaylist -> {
                removeSongFromPlaylist(intent.playlistId, intent.song)
            }
            is PlaylistIntent.AddMultipleSongsToPlaylist -> {
                addMultipleSongsToPlaylist(intent.playlistId, intent.songs)
            }
            is PlaylistIntent.GetPlaylistSongs -> {
                // Intent này chỉ để truy vấn, không cần xử lý async
                // Có thể sử dụng các phương thức tiện ích đã tạo
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

    private fun addSongToPlaylist(playlistId: String, song: Song) {
        viewModelScope.launch {
            try {
                val currentPlaylists = _state.value.playlists
                val updatedPlaylists = currentPlaylists.map { playlist ->
                    if (playlist.id == playlistId) {
                        playlist.copy(songs = playlist.songs + song)
                    } else {
                        playlist
                    }
                }
                _state.update { it.copy(playlists = updatedPlaylists, error = null) }
                _effect.send(PlaylistEffect.ShowMessage("Song '${song.title}' added to playlist"))
            } catch (e: Exception) {
                _state.update { it.copy(error = "Failed to add song to playlist") }
            }
        }
    }

    private fun removeSongFromPlaylist(playlistId: String, song: Song) {
        viewModelScope.launch {
            try {
                val currentPlaylists = _state.value.playlists
                val updatedPlaylists = currentPlaylists.map { playlist ->
                    if (playlist.id == playlistId) {
                        playlist.copy(songs = playlist.songs.filter { it.id != song.id })
                    } else {
                        playlist
                    }
                }
                _state.update { it.copy(playlists = updatedPlaylists, error = null) }
                _effect.send(PlaylistEffect.ShowMessage("Song '${song.title}' removed from playlist"))
            } catch (e: Exception) {
                _state.update { it.copy(error = "Failed to remove song from playlist") }
            }
        }
    }


    fun getPlaylistById(playlistId: String): Playlist? {
        return _state.value.playlists.find { it.id == playlistId }
    }

    fun getSongsInPlaylist(playlistId: String): List<Song> {
        return getPlaylistById(playlistId)?.songs ?: emptyList()
    }


//    fun isSongInPlaylist(playlistId: String, songId: String): Boolean {
//        val playlist = getPlaylistById(playlistId)
//        return playlist?.songs?.any { it.id == songId } ?: false
//    }


    fun getSongCountInPlaylist(playlistId: String): Int {
        return getSongsInPlaylist(playlistId).size
    }

    fun addMultipleSongsToPlaylist(playlistId: String, songs: List<Song>) {
        viewModelScope.launch {
            try {
                val currentPlaylists = _state.value.playlists
                val updatedPlaylists = currentPlaylists.map { playlist ->
                    if (playlist.id == playlistId) {
                        val newSongs = songs.filter { newSong ->
                            !playlist.songs.any { existingSong -> existingSong.id == newSong.id }
                        }
                        playlist.copy(songs = playlist.songs + newSongs)
                    } else {
                        playlist
                    }
                }
                _state.update { it.copy(playlists = updatedPlaylists, error = null) }
                _effect.send(PlaylistEffect.ShowMessage("${songs.size} songs added to playlist"))
            } catch (e: Exception) {
                _state.update { it.copy(error = "Failed to add songs to playlist") }
            }
        }
    }

}