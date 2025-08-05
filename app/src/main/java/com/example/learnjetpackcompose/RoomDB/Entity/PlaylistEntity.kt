package com.example.learnjetpackcompose.RoomDB.Entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "playlist_table",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["userId"])]
)

data class Playlist(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Int = 0,
    val title: String,
    val songs: List<Song>,
    val imageUrl: String? = null,
    val userId: Int
){
    val songCount: Int
        get() = songs.size
}