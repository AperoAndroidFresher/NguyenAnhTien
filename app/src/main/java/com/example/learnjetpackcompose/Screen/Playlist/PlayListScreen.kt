package com.example.learnjetpackcompose.Screen.Playlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnjetpackcompose.R
import com.example.learnjetpackcompose.model.Playlist
import com.example.learnjetpackcompose.model.Song

@Composable
fun PlaylistScreen(
    viewModel: PlaylistViewModel,
    listSongs: List<Song>,
    modifier: Modifier = Modifier
) {

}

@Composable
fun PlaylistCardList(
    playlist: Playlist,
    onRemovePlaylist: (Playlist) -> Unit,
    onRenamePlaylist: (Playlist) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDropdownMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Black),
        ) {
            if (playlist.imageUrl != null) {
                Image(
                    painter = painterResource(id = playlist.imageUrl),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Fit
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.music_note),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                )
            }

            Column {
                Text(
                    text = playlist.name,
                    modifier = Modifier
                        .padding(10.dp)
                        .width(150.dp)
                        .basicMarquee(),
                    style = MaterialTheme.typography.titleSmall,
                    fontSize = 20.sp,
                    color = Color.White
                )
                Text(
                    text = playlist.songCount.toString(),
                    modifier = Modifier.padding(start = 10.dp),
                    color = Color.White.copy(alpha = 0.7f),
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                IconButton(
                    onClick = {
                        showDropdownMenu = true
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Mở menu tùy chọn",
                        tint = Color.White
                    )
                }

                DropdownMenu(
                    shape = RoundedCornerShape(14.dp),
                    expanded = showDropdownMenu,
                    onDismissRequest = { showDropdownMenu = false },
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.DarkGray)
                ) {
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.icon_remove),
                                    contentDescription = "Remove playlist",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Remove playlist",
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        },
                        onClick = {
                            onRemovePlaylist(playlist)
                            showDropdownMenu = false
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.icon_rename),
                                    contentDescription = "Rename playlist",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Rename",
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        },
                        onClick = {
                            onRenamePlaylist(playlist)
                            showDropdownMenu = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PlaylistScreen(
    modifier: Modifier,
    playlists: List<Playlist>,
    onToggleView: () -> Unit,
    onRemovePlaylist: (Playlist) -> Unit,
    onRenamePlaylist: (Playlist) -> Unit
) {
    val listState = rememberLazyListState()
    Column(
        modifier = modifier
            .background(color = Color.Black)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .clickable(
                    onClick = onToggleView
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.width(26.dp))
            Text(
                text = "MY PLAYLIST",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(10.dp),
                color = Color.White
            )

            IconButton(
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.CenterVertically)
                    .size(40.dp),
                onClick = {
                    println("More button clicked")
                },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_add),
                    contentDescription = "Menu",
                    tint = Color.White
                )
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(8.dp),
        ) {
            items(playlists) { playlist ->
                PlaylistCardList(
                    playlist = playlist,
                    onRemovePlaylist = onRemovePlaylist,
                    onRenamePlaylist = onRenamePlaylist
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPlaylistScreen() {
    val samplePlaylists = remember { mutableStateListOf<Playlist>() } // Sử dụng mutableStateListOf để dễ dàng thay đổi dữ liệu test

    // Để test trường hợp có playlist
     samplePlaylists.add(Playlist("1", "My Playlist", 0, R.drawable.rose))
    PlaylistScreen(
        modifier = Modifier,
        playlists = samplePlaylists,
        onToggleView = {},
        onRemovePlaylist = {},
        onRenamePlaylist = {}
    )
}