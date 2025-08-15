// HomeContract.kt
package com.example.learnjetpackcompose.Screen.Home

import androidx.compose.ui.graphics.Color
import com.example.learnjetpackcompose.RoomDB.Entity.Song
import com.example.learnjetpackcompose.data.api.Album
import com.example.learnjetpackcompose.data.api.ArtistInfo
import com.example.learnjetpackcompose.data.api.TopAlbums
import com.example.learnjetpackcompose.data.api.TopTracks
import com.example.learnjetpackcompose.data.api.Artists
import com.example.learnjetpackcompose.data.api.Attr
import com.example.learnjetpackcompose.data.api.Track

data class HomeState(
    val trackCardColors: List<Color> = listOf(
        Color(0xFF777777),
        Color(0xFFFA7777),
        Color(0xFF4462FF),
        Color(0xFF14FF00),
        Color(0xFFE231FF),
        Color(0xFF00FFFF),
        Color(0xFFFB003C),
        Color(0xFFF2A5FF)
    ),
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