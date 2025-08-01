package com.example.learnjetpackcompose.Screen.Playlist.Song

import com.example.learnjetpackcompose.model.Song

data class SongState(
    val songs: List<Song> = emptyList(),
    val isGridView: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface SongIntent {
    data class LoadSongs(val songs: List<Song>) : SongIntent
    data object ToggleView : SongIntent
    data class RemoveSong(val song: Song) : SongIntent
    data class ReorderSongs(val fromIndex: Int, val toIndex: Int) : SongIntent
}

sealed interface SongEffect {
    data class ShowMessage(val message: String) : SongEffect
}