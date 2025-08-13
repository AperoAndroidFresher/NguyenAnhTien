package com.example.learnjetpackcompose.domain.repository

import com.example.learnjetpackcompose.data.api.TopAlbumsResponse
import com.example.learnjetpackcompose.data.api.TopArtistsResponse
import com.example.learnjetpackcompose.data.api.TopTracksResponse

interface TopMusicRepository {
    suspend fun getTopAlbums(apiKey: String, mbid: String): TopAlbumsResponse
    suspend fun getTopTracks(apiKey: String, mbid: String): TopTracksResponse
    suspend fun getTopArtists(apiKey: String): TopArtistsResponse
}