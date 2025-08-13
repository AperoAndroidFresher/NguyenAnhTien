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
    val isPlaying: Boolean = false,
    val showPlayerBar: Boolean = false,
    val currentPlayingSong: Song? = null,
    val showPermissionDialog: Boolean = false
)

sealed interface LibraryIntent {
    data class LoadSongs(val songs: List<Song>) : LibraryIntent
    data class SelectSource(val source: LibrarySource) : LibraryIntent
    data class AddSongToPlaylist(val song: Song) : LibraryIntent
    data class PlaySong(val song: Song) : LibraryIntent
    data object LoadLocalSongs : LibraryIntent
    data object LoadRemoteSongs : LibraryIntent
    data object RefreshAllSongs : LibraryIntent
    data class ShareSong(val song: Song): LibraryIntent
    data object DismissDialog : LibraryIntent
    data object PauseMusic : LibraryIntent
    data object ResumeMusic : LibraryIntent
    data object StopMusic : LibraryIntent
    data class UpdatePlaybackState(val isPlaying: Boolean, val currentSong: Song?) : LibraryIntent
    data object CheckStoragePermission : LibraryIntent
    data object RequestStoragePermission : LibraryIntent
    data object DismissPermissionDialog : LibraryIntent
    data object OnPermissionGranted : LibraryIntent
}

sealed interface LibraryEffect {
    data class ShowMessage(val message: String) : LibraryEffect
    data class ShowDialogChoosePlaylist(val song: Song, val playlists: List<Playlist>) : LibraryEffect
    data class ShareSongFile(val song: Song) : LibraryEffect
    data object NavigateToPlayer : LibraryEffect
    data object RequestStoragePermission : LibraryEffect
}

enum class LibrarySource {
    LOCAL,
    REMOTE
}