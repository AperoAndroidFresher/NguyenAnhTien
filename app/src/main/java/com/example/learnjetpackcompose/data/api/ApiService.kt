package com.example.learnjetpackcompose.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("Remote_audio.json")
    suspend fun getRemoteSongs(): Response<List<RemoteSongDto>>

    @GET("2.0/")
    suspend fun getTopAlbums(
        @Query("api_key") apiKey: String,
        @Query("format") format: String,
        @Query("method") method: String,
        @Query("mbid") mbid: String
    ): TopAlbumsResponse

    @GET("2.0/")
    suspend fun getTopTracks(
        @Query("api_key") apiKey: String,
        @Query("format") format: String,
        @Query("method") method: String,
        @Query("mbid") mbid: String
    ): TopTracksResponse

    @GET("2.0/")
    suspend fun getTopArtists(
        @Query("api_key") apiKey: String,
        @Query("format") format: String,
        @Query("method") method: String
    ): TopArtistsResponse
}