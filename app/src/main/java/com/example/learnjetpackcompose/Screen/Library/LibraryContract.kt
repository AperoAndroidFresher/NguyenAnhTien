package com.example.learnjetpackcompose.Screen.Library

import com.example.learnjetpackcompose.RoomDB.Entity.Playlist
import com.example.learnjetpackcompose.RoomDB.Entity.Song


data class LibraryState(
    val songs: List<Song> = emptyList(),
    val filteredSongs: List<Song> = emptyList(),
    val selectedSource: LibrarySource = LibrarySource.LOCAL,
    val isLoading: Boolean = false,
    val error: String? = null,
    val playlists: List<Playlist>? = null,
    val showDialog: Boolean = false,
    val selectedSong: Song? = null,
)

sealed interface LibraryIntent {
    data class LoadSongs(val songs: List<Song>) : LibraryIntent
    data class SelectSource(val source: LibrarySource) : LibraryIntent
    data class AddSongToPlaylist(val song: Song) : LibraryIntent
    data object LoadLocalSongs : LibraryIntent
    data object LoadRemoteSongs : LibraryIntent
    data class ShareSong(val song: Song): LibraryIntent
    data object DismissDialog : LibraryIntent
}

sealed interface LibraryEffect {
    data class ShowMessage(val message: String) : LibraryEffect
    data class ShowDialogChoosePlaylist(val song: Song, val playlists: List<Playlist>) : LibraryEffect
}

enum class LibrarySource {
    LOCAL,
    REMOTE
}