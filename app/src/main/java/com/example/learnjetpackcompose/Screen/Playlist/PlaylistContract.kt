package com.example.learnjetpackcompose.Screen.Playlist

import com.example.learnjetpackcompose.model.Playlist


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
}

sealed interface PlaylistEffect {
    data class ShowMessage(val message: String) : PlaylistEffect
}