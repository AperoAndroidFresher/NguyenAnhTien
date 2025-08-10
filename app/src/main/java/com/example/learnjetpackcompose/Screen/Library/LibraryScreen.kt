package com.example.learnjetpackcompose.Screen.Library

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.learnjetpackcompose.Component.DropdownMenuItemWithIcon
import com.example.learnjetpackcompose.R
import com.example.learnjetpackcompose.RoomDB.Entity.Song
import com.example.learnjetpackcompose.Screen.Playlist.Component.ChoosePlaylistDialog
import com.example.learnjetpackcompose.Screen.Playlist.PlaylistIntent
import com.example.learnjetpackcompose.Screen.Playlist.PlaylistViewModel

@Composable
fun LibraryScreen(
    libraryViewModel: LibraryViewModel,
    playlistViewModel: PlaylistViewModel,
    songs: List<Song>,
    onNavigateToPlaylist: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by libraryViewModel.state.collectAsState()
    val playlistState by playlistViewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var selectedSongId by remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(playlistState.playlists) {
        libraryViewModel.updatePlaylists(playlistState.playlists)
    }

    LaunchedEffect(songs) {
        libraryViewModel.processIntent(LibraryIntent.LoadSongs(songs))
    }
    LaunchedEffect(state.selectedSource) {
        when (state.selectedSource) {
            LibrarySource.LOCAL -> Unit
            LibrarySource.REMOTE -> libraryViewModel.processIntent(LibraryIntent.LoadRemoteSongs)
        }
    }

    LaunchedEffect(Unit) {
        libraryViewModel.effect.collect { effect ->
            when (effect) {
                is LibraryEffect.ShowMessage -> snackbarHostState.showSnackbar(effect.message)
                is LibraryEffect.ShowDialogChoosePlaylist -> {}
                LibraryEffect.NavigateToPlayer -> Unit
            }
        }
    }

    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header()

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SourceButton(
                    label = "local",
                    source = LibrarySource.LOCAL,
                    selectedSource = state.selectedSource,
                    onClick = {
                        libraryViewModel.processIntent(LibraryIntent.LoadLocalSongs)

                    }
                )
                SourceButton(
                    label = "Remote",
                    source = LibrarySource.REMOTE,
                    selectedSource = state.selectedSource,
                    onClick = {
                        libraryViewModel.processIntent(LibraryIntent.LoadRemoteSongs)
                    }
                )
            }

            state.error?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }

            if (state.isLoading) {
                LoadingWithLottie()
            } else if (state.filteredSongs.isEmpty()) {
                // Debug: Log empty songs
                LaunchedEffect(state.selectedSource, state.songs.size) {
                    println("DEBUG: filteredSongs is empty. Source: ${state.selectedSource}, Total songs: ${state.songs.size}")
                    state.songs.forEach { song ->
                        println("DEBUG: Song path: ${song.data}")
                    }
                }
                ContentLoadFailure()
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(8.dp),
                ) {
                    items(state.filteredSongs) { song ->
                        LibrarySongCardList(
                            song = song,
                            isSelected = selectedSongId == song.songId,
                            onAddToPlaylist = {
                                libraryViewModel.processIntent(
                                    LibraryIntent.AddSongToPlaylist(
                                        it
                                    )
                                )
                            },
                            onPlaySong = {
                                selectedSongId = it.songId
                                libraryViewModel.processIntent(LibraryIntent.PlaySong(it))
                            },
                            onSelectSong = { selectedSongId = it.songId }
                        )
                    }
                }
            }

        }

        if (state.showDialog && state.selectedSong != null) {
            println("DEBUG: Rendering ChoosePlaylistDialog - showDialog: ${state.showDialog}, selectedSong: ${state.selectedSong?.title}")
            ChoosePlaylistDialog(
                playlists = state.playlists ?: emptyList(),
                onDismissRequest = {
                    libraryViewModel.processIntent(LibraryIntent.DismissDialog)
                },
                onPlaylistSelected = { playlist ->
                    playlistViewModel.processIntent(
                        PlaylistIntent.AddSongToPlaylist(
                            playlist.playlistId,
                            state.selectedSong!!
                        )
                    )
                    libraryViewModel.processIntent(LibraryIntent.DismissDialog)
                },
                onAddPlaylistClicked = {
                    libraryViewModel.processIntent(LibraryIntent.DismissDialog)
                    onNavigateToPlaylist()
                }
            )
        }
    }
}

@Composable
fun Header() {
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
}

@Composable
fun GroupButton(
    loadLocalSongs: () -> Unit,
    loadRemoteSongs: () -> Unit,
    state: LibraryState
) {
    Row(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        SourceButton(
            label = "local",
            source = LibrarySource.LOCAL,
            selectedSource = state.selectedSource,
            onClick = loadLocalSongs
        )
        SourceButton(
            label = "Remote",
            source = LibrarySource.REMOTE,
            selectedSource = state.selectedSource,
            onClick = loadRemoteSongs
        )
    }
}

@Composable
fun ContentLoadFailure(
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No internet connection,\nplease check your \nconnection again",
            color = Color.White,
            style = MaterialTheme.typography.titleSmall,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00C2CB),
                contentColor = Color.White,
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.width(150.dp)
        ) {
            Text(text = "Try Again")
        }
    }
}

@Composable
fun SourceButton(
    label: String,
    source: LibrarySource,
    selectedSource: LibrarySource,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selectedSource == source) Color(0xFF00C2CB) else Color.Gray,
            contentColor = Color.White,
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.width(150.dp),
    ) {
        Text(text = label, fontSize = 20.sp)
    }
}

@Composable
fun LoadingWithLottie() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_remote_item_loading)
    )
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(150.dp)
        )
    }
}

@Composable
fun LibrarySongCardList(
    song: Song,
    isSelected: Boolean,
    onAddToPlaylist: (Song) -> Unit,
    onPlaySong: (Song) -> Unit,
    onSelectSong: (Song) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDropdownMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color.DarkGray else Color.Black
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable {
                        onSelectSong(song)
                        onPlaySong(song)
                    }
            ) {

                SongAlbumArt(albumArtUri = song.albumArt)

                SongInfo(song.title, song.artist)

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
                        painter = painterResource(R.drawable.icon_morevert),
                        contentDescription = "Mở menu tùy chọn",
                        tint = Color.White
                    )
                }

                CustomDropDownMenu(
                    expanded = showDropdownMenu,
                    onDismissRequest = { showDropdownMenu = false },
                    onAddToPlaylistClick = {
                        onAddToPlaylist(song)
                        showDropdownMenu = false},
                    onShareClick = {
                        showDropdownMenu = false
                    }
                )
            }
        }
    }
}

@Composable
fun SongAlbumArt(albumArtUri: String?) {
    val uri = if (albumArtUri.isNullOrEmpty() || albumArtUri == Uri.EMPTY.toString()) {
        null
    } else {
        Uri.parse(albumArtUri)
    }

    AsyncImage(
        model = uri,
        contentDescription = "Album Art",
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Crop,
        placeholder = painterResource(R.drawable.icon_music),
        error = painterResource(R.drawable.icon_music),
        fallback = painterResource(R.drawable.icon_music)
    )
}

@Composable
fun SongInfo(title: String, artist: String) {
    Column(modifier = Modifier.padding(start = 10.dp)) {
        Text(
            text = title,
            modifier = Modifier
                .width(170.dp)
                .basicMarquee(),
            style = MaterialTheme.typography.titleSmall,
            fontSize = 16.sp,
            color = Color.White
        )
        Text(
            text = artist,
            modifier = Modifier.width(170.dp)
                .basicMarquee(),
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp
        )
    }
}

@Composable
fun CustomDropDownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onAddToPlaylistClick: () -> Unit,
    onShareClick: () -> Unit
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
            iconId = R.drawable.add_to_playlist,
            contentDescription = "Add to playlist",
            text = "Add to playlist",
            onClick = onAddToPlaylistClick
        )

        DropdownMenuItemWithIcon(
            iconId = R.drawable.icon_share,
            contentDescription = "Share",
            text = "Share",
            onClick = onShareClick
        )

    }

}

