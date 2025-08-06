package com.example.learnjetpackcompose.RoomDB.Entity

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.room.Entity
import androidx.room.PrimaryKey
import getAllMp3Files

@Entity(tableName = "songs")
data class Song(
    @PrimaryKey val songId: Long,
    val title: String,
    val artist: String,
    val albumArt: String?,
    val duration: String,
    val data: String
)

class SongViewModel(application: Application) : AndroidViewModel(application) {
    val songs: List<Song> = getAllMp3Files(application)
}