// HomeContract.kt
package com.example.learnjetpackcompose.Screen.Home

import com.example.learnjetpackcompose.RoomDB.Entity.Song
import com.example.learnjetpackcompose.data.api.Album
import com.example.learnjetpackcompose.data.api.ArtistInfo
import com.example.learnjetpackcompose.data.api.TopAlbums
import com.example.learnjetpackcompose.data.api.TopTracks
import com.example.learnjetpackcompose.data.api.Artists
import com.example.learnjetpackcompose.data.api.Attr
import com.example.learnjetpackcompose.data.api.Track

data class HomeState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val displayName: String = "",
    val userAvatar: String? = null,
    val topAlbums: List<Album> = emptyList(),
    val topTracks: List<Track> = emptyList(),
    val topArtists: List<ArtistInfo> = emptyList(),
    val allAlbums: TopAlbums = TopAlbums(emptyList(), Attr("", "", "", "", "")),
    val allTracks: TopTracks = TopTracks(emptyList(), Attr("", "", "", "", "")),
    val allArtists: Artists = Artists(emptyList(), Attr("", "", "", "", ""))
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