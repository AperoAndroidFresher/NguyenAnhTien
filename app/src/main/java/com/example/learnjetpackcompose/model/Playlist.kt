package com.example.learnjetpackcompose.model

data class Playlist(
    val id: String,
    val title: String,
    val songCount: Int,
    val imageUrl: Int? = null
)