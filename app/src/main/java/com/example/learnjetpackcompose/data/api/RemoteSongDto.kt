package com.example.learnjetpackcompose.data.api

import com.google.gson.annotations.SerializedName

data class RemoteSongDto(
    @SerializedName("title")
    val title: String,
    @SerializedName("artist")
    val artist: String,
    @SerializedName("kind")
    val kind: String,
    @SerializedName("duration")
    val duration: String,
    @SerializedName("path")
    val path: String
)

