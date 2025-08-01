package com.example.learnjetpackcompose.Screen.Library

import com.example.learnjetpackcompose.model.Song

data class LibraryState(
    val songs: List<Song> = emptyList(),
    val filteredSongs: List<Song> = emptyList(),
    val selectedSource: LibrarySource = LibrarySource.LOCAL,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface LibraryIntent {
    data class LoadSongs(val songs: List<Song>) : LibraryIntent
    data class SelectSource(val source: LibrarySource) : LibraryIntent
    data class RemoveSong(val song: Song) : LibraryIntent
    data object LoadLocalSongs : LibraryIntent
    data object LoadRemoteSongs : LibraryIntent
}

sealed interface LibraryEffect {
    data class ShowMessage(val message: String) : LibraryEffect
    data object NavigateToPlaylist : LibraryEffect
}

enum class LibrarySource {
    LOCAL,
    REMOTE
}