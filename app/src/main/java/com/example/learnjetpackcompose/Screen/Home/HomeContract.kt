package com.example.learnjetpackcompose.Screen.Home

import com.example.learnjetpackcompose.RoomDB.Entity.Song


data class HomeState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val displayName: String = "",
    val userAvatar: String? = null,
    val topAlbums: List<HomeAlbum> = emptyList(),
    val topTracks: List<Song> = emptyList(),
    val topArtists: List<HomeArtist> = emptyList(),
    val allAlbums: List<HomeAlbum> = emptyList(),
    val allTracks: List<Song> = emptyList(),
    val allArtists: List<HomeArtist> = emptyList()
)

data class HomeAlbum(
    val title: String,
    val artist: String,
    val coverUri: String?
)

data class HomeArtist(
    val name: String,
    val avatarUri: String?
)

sealed interface HomeIntent {
    data object LoadData : HomeIntent
    data object ShowAllAlbums : HomeIntent
    data object ShowAllTracks : HomeIntent
    data object ShowAllArtists : HomeIntent
}

sealed interface HomeEffect {
    data class ShowMessage(val message: String) : HomeEffect
}


