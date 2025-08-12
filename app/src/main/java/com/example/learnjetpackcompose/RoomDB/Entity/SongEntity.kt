package com.example.learnjetpackcompose.RoomDB.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class Song(
    @PrimaryKey val songId: Long,
    val title: String,
    val artist: String,
    val albumArt: String?,
    val duration: String,
    val data: String
)