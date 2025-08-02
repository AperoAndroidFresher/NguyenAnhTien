package com.example.learnjetpackcompose.Screen.Playlist
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.learnjetpackcompose.R
import com.example.learnjetpackcompose.model.Playlist


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
                    modifier = Modifier.padding(bottom = 16.dp).align(Alignment.CenterHorizontally)
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
        modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp)
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
    LazyColumn {
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
        // Hình ảnh playlist (nếu có)
        if (playlist.imageUrl != null) {
            Image(
                painter = painterResource(id = playlist.imageUrl),
                contentDescription = "Playlist Cover",
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 16.dp)
            )
        } else {
            // Placeholder nếu không có ảnh
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 16.dp)
                    .background(Color.Gray)
            )
        }

        Column {
            Text(
                text = playlist.name,
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

// Cách sử dụng trong Composable của bạn
@Preview(showBackground = true)
@Composable
fun PreviewChoosePlaylistDialog() {
    val samplePlaylists = remember { mutableStateListOf<Playlist>() }

    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        ChoosePlaylistDialog(
            playlists = samplePlaylists,
            onDismissRequest = { showDialog = false },
            onPlaylistSelected = { playlist ->
                println("Selected playlist: ${playlist.name}")
                showDialog = false
            },
            onAddPlaylistClicked = {
                println("Add new playlist clicked")
                // Thêm một playlist mới để test
                samplePlaylists.add(Playlist(
                    id = (samplePlaylists.size + 1).toString(),
                    name = "New Playlist ${samplePlaylists.size + 1}",
                    songCount = 0,
                    imageUrl = R.drawable.rose
                ))
            }
        )
    }


}