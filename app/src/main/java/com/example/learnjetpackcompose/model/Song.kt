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

//val songs = getAllMp3Files()
//val songs = listOf(
//    Song("Ordinary", "Alex Warren", "3:45", R.drawable.rose),
//    Song("APT", "ROSE, Bruno Mars", "4:20", R.drawable.music1),
//    Song("Azizam", "Ed Sheeran", "5:10", R.drawable.music2),
//    Song("Bad Dreams", "Teddy Swims", "3:55", R.drawable.music3),
//    Song("Anxiety", "Doechii", "4:40", R.drawable.music4),
//    Song("Messy", "Rose", "5:25", R.drawable.music5),
//    Song("BIRDS OF A FEATHER", "Billie Eilish", "3:30", R.drawable.music1),
//    Song("back to friends", "sombr", "4:15", R.drawable.music2),
//    Song("Sorry I'm Here for someone Else", "Benson Boone", "5:05", R.drawable.music3),
//    Song("More to Lose", "Miley Cyrus", "3:50", R.drawable.music4),
//    Song("Love Me Not", "Ravyn Lenae", "4:35", R.drawable.music5),
//    Song("No One Noticed", "The Marias", "5:20", R.drawable.rose),
//    Song("Die With A Smile", "Lady Gaga, Bruno Mars", "3:40", R.drawable.music2),
//    Song("Luther", "Kendrick Lamar", "4:25", R.drawable.music3),
//    Song("Lose Control", "Teddy Swims", "5:15", R.drawable.music4)
//)