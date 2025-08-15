package com.example.learnjetpackcompose.data.api

data class TopTracksResponse(
    val toptracks: TopTracks
)

data class TopTracks(
    val track: List<Track>,
    val attr: Attr // Using 'attr' instead of '@attr'
)

data class Track(
    val name: String,
    val playcount: String,
    val listeners: String,
    val url: String,
    val streamable: String,
    val artist: Artist,
    val image: List<Image>,
    val attr: RankAttr // Using 'attr' instead of '@attr'
)

data class RankAttr(
    val rank: String
)