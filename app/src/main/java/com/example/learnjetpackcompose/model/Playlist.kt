package com.example.learnjetpackcompose.model

data class Playlist(
    val id: String,
    val title: String,
    val songs: List<Song> = emptyList(),
    val imageUrl: Int? = null
){
    val songCount: Int
        get() = songs.size
}