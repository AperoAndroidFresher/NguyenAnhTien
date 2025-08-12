package com.example.learnjetpackcompose.Screen.Playlist.Song.Component

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnjetpackcompose.Component.AlbumArt
import com.example.learnjetpackcompose.RoomDB.Entity.Song

@Composable
fun SongCardGrid(
    song: Song,
    isSelected: Boolean,
    onRemoveSong: (Song) -> Unit,
    onPlaySong: (Song) -> Unit,
    onSelectSong: (Song) -> Unit,
    onShareSong: (Song) -> Unit
){
    var showDropdownMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color.DarkGray else Color.Black
        )
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onSelectSong(song)
                    onPlaySong(song)
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Box(){
                AlbumArt(song.albumArt, Modifier.size(140.dp))

                SongMoreOptions({showDropdownMenu = true}, onRemoveSong, onShareSong, {showDropdownMenu = false},
                    song, showDropdownMenu, Modifier.align(Alignment.TopEnd).clip(CircleShape)
                        .background(Color.DarkGray.copy(0.6f)))
            }
            SongGridInfo(song.title, song.artist, song.duration)
        }
    }
}


@Composable
private fun SongGridInfo(
    title: String,
    artist: String,
    duration: String
){
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = title,
            modifier = Modifier.padding(start = 10.dp).align(Alignment.CenterHorizontally).basicMarquee(),
            style = MaterialTheme.typography.titleSmall,
            fontSize = 18.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = artist,
            modifier = Modifier.padding(start = 10.dp).basicMarquee(),
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 14.sp

        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = duration,
            color = Color.White,
            fontSize = 16.sp
        )
    }
}

