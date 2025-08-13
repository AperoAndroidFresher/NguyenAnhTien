package com.example.learnjetpackcompose.data.api

data class TopAlbumsResponse(
    val topalbums: TopAlbums
)

data class TopAlbums(
    val album: List<Album>,
    val attr: Attr
)

data class Album(
    val name: String,
    val playcount: Int,
    val url: String,
    val artist: Artist,
    val image: List<Image>,
    val attr: AlbumAttr
)

data class AlbumAttr(
    val rank: String
)