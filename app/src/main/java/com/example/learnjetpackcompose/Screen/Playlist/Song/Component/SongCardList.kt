package com.example.learnjetpackcompose.Screen.Playlist.Song.Component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
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
import com.example.learnjetpackcompose.Component.DropdownMenuItemWithIcon
import com.example.learnjetpackcompose.Component.IconButtonCustom
import com.example.learnjetpackcompose.Component.SongInfo
import com.example.learnjetpackcompose.R
import com.example.learnjetpackcompose.RoomDB.Entity.Song

@Composable
fun SongCardList(
    song: Song,
    isSelected: Boolean,
    onRemoveSong: (Song) -> Unit,
    onPlaySong: (Song) -> Unit,
    onSelectSong: (Song) -> Unit,
    onShareSong: (Song) -> Unit,
    modifier: Modifier = Modifier
){
    var showDropdownMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color.DarkGray else Color.Black
        )
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clickable {
                    onSelectSong(song)
                    onPlaySong(song)
                },
            verticalAlignment = Alignment.CenterVertically
        ){
            AlbumArt(song.albumArt)

            SongInfo(song.title, song.artist)

            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = song.duration,
                modifier = Modifier.align(Alignment.CenterVertically),
                color = Color.White,
                fontSize = 14.sp
            )
            SongMoreOptions({showDropdownMenu = true}, onRemoveSong, onShareSong, {showDropdownMenu = false},
                song, showDropdownMenu, Modifier.align(Alignment.CenterVertically))
//            Box(
//                modifier = Modifier.align(Alignment.CenterVertically)
//            ) {
//                IconButtonCustom(
//                    onClick = {showDropdownMenu = true},
//                    icon = R.drawable.icon_morevert,
//                    title = "More options",
//                )
//                DropdownMenu(
//                    shape = RoundedCornerShape(14.dp),
//                    expanded = showDropdownMenu,
//                    onDismissRequest = { showDropdownMenu = false },
//                    modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(Color.DarkGray)
//                ) {
//                    DropdownMenuItemWithIcon(
//                        iconId = R.drawable.icon_remove,
//                        contentDescription = "Remove",
//                        text = "Remove from playlist",
//                        onClick = { onRemoveSong(song)
//                            showDropdownMenu = false }
//                    )
//                    DropdownMenuItemWithIcon(
//                        iconId = R.drawable.icon_share,
//                        contentDescription = "Remove",
//                        text = "Share",
//                        onClick = { onShareSong(song); showDropdownMenu = false }
//                    )
//                }
//            }
        }
    }
}
