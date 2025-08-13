package com.example.learnjetpackcompose.data.api

data class TopArtistsResponse(
    val artists: Artists
)

data class Artists(
    val artist: List<ArtistInfo>,
    val attr: Attr
)

data class ArtistInfo(
    val name: String,
    val playcount: String,
    val listeners: String,
    val mbid: String,
    val url: String,
    val streamable: String,
    val image: List<Image>
)