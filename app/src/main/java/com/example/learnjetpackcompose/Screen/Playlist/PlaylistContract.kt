package com.example.learnjetpackcompose.Screen.Playlist

import com.example.learnjetpackcompose.model.Playlist
import com.example.learnjetpackcompose.model.Song


data class PlaylistState(
    val playlists: List<Playlist> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface PlaylistIntent {
    data class LoadPlaylists(val playlists: List<Playlist>) : PlaylistIntent
    data class AddPlaylist(val playlist: Playlist) : PlaylistIntent
    data class RemovePlaylist(val playlist: Playlist) : PlaylistIntent
    data class RenamePlaylist(val playlist: Playlist) : PlaylistIntent
    data class AddSongToPlaylist(val playlistId: String, val song: Song) : PlaylistIntent
    data class RemoveSongFromPlaylist(val playlistId: String, val song: Song): PlaylistIntent
    data class AddMultipleSongsToPlaylist(val playlistId: String, val songs: List<Song>) : PlaylistIntent
    data class GetPlaylistSongs(val playlistId: String) : PlaylistIntent
}

sealed interface PlaylistEffect {
    data class ShowMessage(val message: String) : PlaylistEffect
}