package com.example.learnjetpackcompose.Screen.Playlist

import com.example.learnjetpackcompose.RoomDB.Entity.Playlist
import com.example.learnjetpackcompose.RoomDB.Entity.Song


data class PlaylistState(
    val playlists: List<Playlist> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface PlaylistIntent {
    data class LoadPlaylists(val playlists: List<Playlist>) : PlaylistIntent
    data class AddPlaylist(val title: String) : PlaylistIntent
    data class RemovePlaylist(val playlist: Playlist) : PlaylistIntent
    data class RenamePlaylist(val playlist: Playlist) : PlaylistIntent
    data class AddSongToPlaylist(val playlistId: Int, val song: Song) : PlaylistIntent
    data class RemoveSongFromPlaylist(val playlistId: Int, val song: Song): PlaylistIntent
    data class GetPlaylistSongs(val playlistId: Int) : PlaylistIntent
}

sealed interface PlaylistEffect {
    data class ShowMessage(val message: String) : PlaylistEffect
}