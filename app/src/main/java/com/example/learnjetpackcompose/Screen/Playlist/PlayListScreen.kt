package com.example.learnjetpackcompose.Screen.Playlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnjetpackcompose.Component.AlbumArt
import com.example.learnjetpackcompose.R
import com.example.learnjetpackcompose.RoomDB.Entity.Playlist
import com.example.learnjetpackcompose.Component.DropdownMenuItemWithIcon
import com.example.learnjetpackcompose.Component.IconButtonCustom
import com.example.learnjetpackcompose.Component.SongInfo
import com.example.learnjetpackcompose.Screen.Library.LibraryViewModel
import com.example.learnjetpackcompose.Screen.Playlist.Component.DialogCreatePlaylist
import com.example.learnjetpackcompose.Screen.Playlist.Component.DialogRenamePlaylist


@Composable
fun NoPlaylistScreen(
    onAddPlaylistClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().background(Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "You don't have any\nplaylists. Click the\n\"+\" button to add",
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 20.sp,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Icon(
            painter = painterResource(R.drawable.button_add_playlist),
            contentDescription = "Add",
            tint = Color.White,
            modifier = Modifier
                .size(80.dp)
                .clickable { onAddPlaylistClicked() }
        )
    }
}

@Composable
fun PlaylistCardList(
    playlist: Playlist,
    onRemovePlaylist: (Playlist) -> Unit,
    onRenamePlaylist: (Playlist) -> Unit,
    onPlaylistClick: (Playlist) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDropdownMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable { onPlaylistClick(playlist) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Black),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AlbumArt(playlist.imageUrl)

            SongInfo(playlist.title, playlist.songs.size.toString() + " Songs")

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                IconButtonCustom(onClick = {showDropdownMenu = true},
                    R.drawable.icon_morevert, "More options")
                CustomDropDownMenu(
                    expanded = showDropdownMenu,
                    onDismissRequest = {showDropdownMenu = false},
                    onRemovePlaylistClick = {
                        onRemovePlaylist(playlist)
                        showDropdownMenu = false
                    },
                    onRenamePlaylistClick = {
                        onRenamePlaylist(playlist)
                        showDropdownMenu = false
                    }
                )
            }
        }
    }
}

@Composable
fun PlaylistScreen(
    modifier: Modifier = Modifier,
    viewModel: PlaylistViewModel,
    onNavigateToSongs: (Playlist) -> Unit = {},
) {
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()
    var showCreateDialog by remember { mutableStateOf(false) }
    var showRenameDialog by remember { mutableStateOf<Playlist?>(null) }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.width(26.dp))
            Text(
                text = "MY PLAYLIST",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(10.dp),
                color = Color.White
            )
            IconButtonCustom(onClick = {showCreateDialog = true}, R.drawable.icon_add, "Add playlist",
                modifier = Modifier.size(40.dp))
        }
        if (state.playlists.isEmpty()) {
            NoPlaylistScreen(onAddPlaylistClicked = {
                showCreateDialog = true
            })
        } else {
            LazyColumn(
                contentPadding = PaddingValues(8.dp),
            ) {
                items(state.playlists) { playlist ->
                    PlaylistCardList(
                        playlist = playlist,
                        onRemovePlaylist = { viewModel.processIntent(PlaylistIntent.RemovePlaylist(it)) },
                        onRenamePlaylist = { showRenameDialog = it },
                        onPlaylistClick = { onNavigateToSongs(it) }
                    )
                }
            }
        }
    }

    if (showCreateDialog) {
        DialogCreatePlaylist(
            onDismissRequest = { showCreateDialog = false },
            onCreatePlaylist = { playlistTitle ->

                viewModel.processIntent(PlaylistIntent.AddPlaylist(playlistTitle))
                showCreateDialog = false
            }
        )
    }

    val renameTarget = showRenameDialog
    if (renameTarget != null) {
        DialogRenamePlaylist(
            playlist = renameTarget,
            onDismissRequest = { showRenameDialog = null },
            onConfirmRename = { newTitle ->
                viewModel.processIntent(
                    PlaylistIntent.RenamePlaylist(
                        renameTarget.copy(title = newTitle)
                    )
                )
                showRenameDialog = null
            }
        )
    }
}

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




