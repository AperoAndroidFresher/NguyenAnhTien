package com.example.learnjetpackcompose.Screen.Player

data class PlayerState(
    val songTitle: String = "",
    val artist: String = "",
    val albumArt: String = "",
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0,
    val duration: Long = 0,
)

sealed interface PlayerIntent{
    data object Play : PlayerIntent
    data object Pause : PlayerIntent
    data object Stop : PlayerIntent
    data class Next(val position: Long) : PlayerIntent
    data class Previous(val position: Long) : PlayerIntent
    data object Shuffle : PlayerIntent
    data object Repeat : PlayerIntent
    data class Seek(val position: Long) : PlayerIntent
}

enum class RepeatMode{
    NONE,
    REPEAT_ALL,
    REPEAT_ONE
}

