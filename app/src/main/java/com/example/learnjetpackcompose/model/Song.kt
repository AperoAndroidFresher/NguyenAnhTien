package com.example.learnjetpackcompose.model

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import com.example.learnjetpackcompose.Utils.getAllMp3Files

data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val albumArt: Bitmap?,
    val duration: String,
    val data: String
)

class SongViewModel(application: Application) : AndroidViewModel(application) {
    val songs: List<Song> = getAllMp3Files(application)
}
