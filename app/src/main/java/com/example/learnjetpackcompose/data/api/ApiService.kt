package com.example.learnjetpackcompose.data.api

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("Remote_audio.json")
    suspend fun getRemoteSongs(): Response<List<RemoteSongDto>>
}