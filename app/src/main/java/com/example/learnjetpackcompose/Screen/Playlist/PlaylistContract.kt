package com.example.learnjetpackcompose.Screen.Playlist

import com.example.learnjetpackcompose.model.Song

data class PlaylistState(
    val songs: List<Song> = emptyList(),
    val isGridView: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface PlaylistIntent {
    data class LoadSongs(val songs: List<Song>) : PlaylistIntent
    data object ToggleView : PlaylistIntent
    data class RemoveSong(val song: Song) : PlaylistIntent
    data class ReorderSongs(val fromIndex: Int, val toIndex: Int) : PlaylistIntent
}

sealed interface PlaylistEffect {
    data class ShowMessage(val message: String) : PlaylistEffect
}