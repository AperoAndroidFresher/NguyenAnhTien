package com.example.learnjetpackcompose.data.repository

import com.example.learnjetpackcompose.data.api.ApiService
import com.example.learnjetpackcompose.data.api.TopAlbumsResponse
import com.example.learnjetpackcompose.data.api.TopArtistsResponse
import com.example.learnjetpackcompose.data.api.TopTracksResponse
import com.example.learnjetpackcompose.di.qualifiers.HomeApi
import com.example.learnjetpackcompose.domain.repository.TopMusicRepository
import javax.inject.Inject

class TopMusicRepositoryImpl @Inject constructor(
    @HomeApi private val apiService: ApiService
) : TopMusicRepository {

    override suspend fun getTopAlbums(apiKey: String, mbid: String): TopAlbumsResponse {
        return apiService.getTopAlbums(apiKey, "json", "artist.getTopAlbums", mbid)
    }

    override suspend fun getTopTracks(apiKey: String, mbid: String): TopTracksResponse {
        return apiService.getTopTracks(apiKey, "json", "artist.getTopTracks", mbid)
    }

    override suspend fun getTopArtists(apiKey: String): TopArtistsResponse {
        return apiService.getTopArtists(apiKey, "json", "chart.gettopartists")
    }
}