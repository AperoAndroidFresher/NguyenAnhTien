package com.example.learnjetpackcompose.data.api

import com.google.gson.annotations.SerializedName
data class Artist(
    val name: String,
    val mbid: String,
    val url: String
)

data class Image(
    @SerializedName("#text")
    val text: String, // Using 'text' instead of '#text'
    val size: String
)

data class Attr(
    val artist: String? = null,
    val page: String,
    val perPage: String,
    val totalPages: String,
    val total: String
)