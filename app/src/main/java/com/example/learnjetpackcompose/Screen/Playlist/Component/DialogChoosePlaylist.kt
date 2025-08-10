package com.example.learnjetpackcompose.Screen.Playlist.Component
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.learnjetpackcompose.R
import com.example.learnjetpackcompose.RoomDB.Entity.Playlist



@Composable
fun ChoosePlaylistDialog(
    playlists: List<Playlist>,
    onDismissRequest: () -> Unit,
    onPlaylistSelected: (Playlist) -> Unit,
    onAddPlaylistClicked: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.medium,
            color = Color(0xFF292929)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Choose playlist",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )

                if (playlists.isEmpty()) {
                    NoPlaylistsContent(onAddPlaylistClicked = onAddPlaylistClicked)
                } else {
                    PlaylistListContent(
                        playlists = playlists,
                        onPlaylistSelected = onPlaylistSelected
                    )
                }
            }
        }
    }
}

@Composable
fun NoPlaylistsContent(onAddPlaylistClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp)
            .background(Color(0xFF292929)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "You don't have any\nplaylists. Click the\n\"+\" button to add",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 16.sp,
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
fun PlaylistListContent(
    playlists: List<Playlist>,
    onPlaylistSelected: (Playlist) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
            .padding(10.dp)
    ) {
        items(playlists) { playlist ->
            PlaylistItem(playlist = playlist) {
                onPlaylistSelected(it)
            }
        }
    }
}

@Composable
fun PlaylistItem(playlist: Playlist, onClick: (Playlist) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(playlist) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (playlist.imageUrl != null) {
            AsyncImage(
                model = playlist.imageUrl,
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.LightGray, CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.icon_music),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.LightGray, CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = playlist.title,
                color = Color.White,
                fontSize = 18.sp
            )
            Text(
                text = "${playlist.songCount} songs",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewChoosePlaylistDialog() {
    val samplePlaylists = remember { mutableStateListOf<Playlist>() }
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        ChoosePlaylistDialog(
            playlists = samplePlaylists,
            onDismissRequest = { showDialog = false },
            onPlaylistSelected = { playlist ->
                println("Selected playlist: ${playlist.title}")
                showDialog = false
            },
            onAddPlaylistClicked = {
                println("Add new playlist clicked - Navigate to PlaylistScreen")
                showDialog = false
            }
        )
    }
}