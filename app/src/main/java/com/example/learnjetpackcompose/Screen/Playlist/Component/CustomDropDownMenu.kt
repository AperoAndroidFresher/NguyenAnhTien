package com.example.learnjetpackcompose.Screen.Playlist.Component

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.learnjetpackcompose.Component.DropdownMenuItemWithIcon
import com.example.learnjetpackcompose.R

@Composable
fun CustomDropDownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onRemovePlaylistClick: () -> Unit,
    onRenamePlaylistClick: () -> Unit
){
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color.DarkGray)
    ){
        DropdownMenuItemWithIcon(
            iconId = R.drawable.icon_remove,
            contentDescription = "Remove playlist",
            text = "Remove playlist",
            onClick = onRemovePlaylistClick
        )

        DropdownMenuItemWithIcon(
            iconId = R.drawable.icon_rename,
            contentDescription = "Rename",
            text = "Rename",
            onClick = onRenamePlaylistClick
        )
    }
}