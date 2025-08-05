package com.example.learnjetpackcompose.Screen.Library

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.learnjetpackcompose.R
import com.example.learnjetpackcompose.RoomDB.Entity.Song
import com.example.learnjetpackcompose.Screen.Playlist.ChoosePlaylistDialog
import com.example.learnjetpackcompose.Screen.Playlist.DialogCreatePlaylist
import com.example.learnjetpackcompose.Screen.Playlist.PlaylistIntent
import com.example.learnjetpackcompose.Screen.Playlist.PlaylistViewModel
import com.example.learnjetpackcompose.RoomDB.Entity.Playlist
import kotlinx.coroutines.flow.collectLatest
// onNavigateToPlaylist:() -> Unit,
@Composable
fun LibraryScreen(
    libraryViewModel: LibraryViewModel,
    playlistViewModel: PlaylistViewModel,
    songs: List<Song>,
    onNavigateToPlaylist:() -> Unit,
    modifier: Modifier = Modifier
) {
    val state by libraryViewModel.state.collectAsState()
    val playlistState by playlistViewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showDialog by remember { mutableStateOf(false) }
    var selectedSong by remember { mutableStateOf<Song?>(null) }

    // Cập nhật playlists cho LibraryViewModel khi playlists thay đổi
    LaunchedEffect(playlistState.playlists) {
        libraryViewModel.updatePlaylists(playlistState.playlists)
    }

    // Initialize songs when screen loads
    LaunchedEffect(songs) {
        libraryViewModel.processIntent(LibraryIntent.LoadSongs(songs))
    }

    // Handle side effects - SỬA ĐỔI QUAN TRỌNG Ở ĐÂY
    LaunchedEffect(libraryViewModel) {
        libraryViewModel.effect.collect { effect ->
            when (effect) {
                is LibraryEffect.ShowMessage -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is LibraryEffect.ShowDialogChoosePlaylist -> {
                    selectedSong = effect.song
                    showDialog = true
                }
            }
        }
    }

    // Thêm debug log để kiểm tra
    LaunchedEffect(showDialog, selectedSong) {
        println("DEBUG: showDialog = $showDialog, selectedSong = ${selectedSong?.title}")
    }


    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Library",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Source Selection Buttons
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { libraryViewModel.processIntent(LibraryIntent.LoadLocalSongs) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (state.selectedSource == LibrarySource.LOCAL) Color(0xFF00C2CB) else Color.Gray,
                        contentColor = Color.White,
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.width(150.dp),
                    enabled = !state.isLoading
                ) {
                    if (state.isLoading && state.selectedSource == LibrarySource.LOCAL) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(text = "Local", fontSize = 20.sp)
                    }
                }

                Button(
                    onClick = { libraryViewModel.processIntent(LibraryIntent.LoadRemoteSongs) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (state.selectedSource == LibrarySource.REMOTE) Color(0xFF00C2CB) else Color.Gray,
                        contentColor = Color.White,
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.width(150.dp),
                    enabled = !state.isLoading
                ) {
                    if (state.isLoading && state.selectedSource == LibrarySource.REMOTE) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(text = "Remote", fontSize = 20.sp)
                    }
                }
            }

            // Error Message
            state.error?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }

            // Songs List
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFF00C2CB),
                            modifier = Modifier.size(40.dp),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Loading ${state.selectedSource.name.lowercase()} songs...",
                            color = Color.White.copy(0.7f),
                            fontSize = 14.sp
                        )
                    }
                }
            } else if (state.filteredSongs.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No songs found.", fontSize = 40.sp, color = Color.White)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(8.dp),
                ) {
                    items(state.filteredSongs) { song ->
                        LibrarySongCardList(
                            song = song,
                            onAddToPlaylist = { libraryViewModel.processIntent(LibraryIntent.AddSongToPlaylist(it)) }
                        )
                    }
                }
            }
        }

        // Hiển thị dialog khi showDialog là true
        if (showDialog && selectedSong != null) {
            ChoosePlaylistDialog(
                playlists = state.playlists ?: emptyList(),
                onDismissRequest = {
                    showDialog = false
                    selectedSong = null
                },
                onPlaylistSelected = { playlist ->
                    playlistViewModel.processIntent(PlaylistIntent.AddSongToPlaylist(playlist.playlistId, selectedSong!!))
                    showDialog = false
                    selectedSong = null
                },
                onAddPlaylistClicked = {
                    showDialog = false
                    onNavigateToPlaylist()
                }
            )
        }
    }
}

@Composable
fun LibrarySongCardList(
    song: Song,
    onAddToPlaylist: (Song) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDropdownMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Black),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {


                AsyncImage(
                    model = if (song.albumArt.isNullOrEmpty() || song.albumArt == Uri.EMPTY.toString()) {
                        null
                    } else {
                        Uri.parse(song.albumArt)
                    },
                    contentDescription = "Album Art",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.music_note),
                    error = painterResource(id = R.drawable.music_note),
                    fallback = painterResource(id = R.drawable.music_note)
                )

                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(
                        text = song.title,
                        modifier = Modifier
                            .padding(10.dp)
                            .width(150.dp)
                            .basicMarquee(),
                        style = MaterialTheme.typography.titleSmall,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Text(
                        text = song.artist,
                        modifier = Modifier.padding(start = 10.dp),
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = song.duration,
                    modifier = Modifier.padding(start = 4.dp),
                    color = Color.White,
                    fontSize = 14.sp
                )

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
                                    painter = painterResource(R.drawable.add_to_playlist),
                                    contentDescription = "Add to playlist",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Add to playlist",
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        },
                        onClick = {
                            onAddToPlaylist(song)
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
                                    painter = painterResource(R.drawable.icon_share),
                                    contentDescription = "Share",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Share",
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        },
                        onClick = {
                            println("Share clicked - coming soon")
                            showDropdownMenu = false
                        }
                    )
                }
            }
        }
    }
}