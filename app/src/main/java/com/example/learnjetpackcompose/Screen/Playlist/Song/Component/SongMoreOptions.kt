package com.example.learnjetpackcompose.Screen.Playlist.Song.Component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.learnjetpackcompose.Component.DropdownMenuItemWithIcon
import com.example.learnjetpackcompose.Component.IconButtonCustom
import com.example.learnjetpackcompose.R
import com.example.learnjetpackcompose.RoomDB.Entity.Song

@Composable
fun SongMoreOptions(
    onClick: () -> Unit,
    onRemoveSong: (Song) -> Unit,
    onShareSong: (Song) -> Unit,
    onDissmissRequest: () -> Unit,
    song: Song,
    showDropdownMenu: Boolean,
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier.size(36.dp)
    ) {

        IconButtonCustom(
            onClick = onClick, R.drawable.icon_morevert,
            title = "More options",
        )

        DropdownMenu(
            shape = RoundedCornerShape(14.dp),
            expanded = showDropdownMenu,
            onDismissRequest = onDissmissRequest,
            modifier = Modifier.clip(RoundedCornerShape(14.dp))
                .background(Color.DarkGray.copy(alpha = 0.8f), RoundedCornerShape(14.dp))
        ) {

            DropdownMenuItemWithIcon(
                iconId = R.drawable.icon_remove,
                contentDescription = "Remove",
                text = "Remove from playlist",
                onClick = { onRemoveSong(song) }
            )
            DropdownMenuItemWithIcon(
                iconId = R.drawable.icon_share,
                contentDescription = "Remove",
                text = "Share",
                onClick = { onShareSong(song)}
            )
        }
    }
}