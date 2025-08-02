package com.example.learnjetpackcompose.model

data class Playlist(
    val id: String,
    val name: String,
    val songCount: Int,
    val imageUrl: Int? = null
)