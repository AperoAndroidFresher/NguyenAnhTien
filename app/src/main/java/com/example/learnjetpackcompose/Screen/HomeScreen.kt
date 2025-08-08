package com.example.learnjetpackcompose.Screen

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.learnjetpackcompose.R
import com.example.learnjetpackcompose.RoomDB.Entity.SongViewModel
import com.example.learnjetpackcompose.Screen.Library.LibraryScreen
import com.example.learnjetpackcompose.Screen.Library.LibraryViewModel
import com.example.learnjetpackcompose.Screen.Playlist.PlaylistScreen
import com.example.learnjetpackcompose.Screen.Playlist.PlaylistViewModel
import com.example.learnjetpackcompose.data.model.NavBottomItems

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onMyProfileClick: () -> Unit,
    onPlaylistClick: () -> Unit = {},
    onSongClick: (Int, String) -> Unit = { _, _ -> },
) {
    val navItemsList = listOf(
        NavBottomItems("Home", R.drawable.icon_home),
        NavBottomItems("Library", R.drawable.icon_library),
        NavBottomItems("My Playlist", R.drawable.icon_playlist)
    )
    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                navItemsList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index
                        },
                        icon = {
                            Icon(
                                painter = painterResource(navItem.icon),
                                contentDescription = "Icon page",
                                modifier = Modifier.size(25.dp)
                            )
                        },
                        label = {
                            Text(
                                navItem.label,
                                fontSize = 16.sp,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        ContentScreen(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            selectedIndex = selectedIndex,
            onMyProfileClick = onMyProfileClick,
            onPlaylistClick = onPlaylistClick,
            onSongClick = onSongClick,
        )
    }
}

@Composable
fun HomePage(
    modifier: Modifier,
    onMyProfileClick: () -> Unit,
    onPlaylistClick: () -> Unit = {}
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.width(10.dp))
            IconButton(
                modifier = Modifier
                    .padding(10.dp)
                    .size(40.dp)
                    .align(Alignment.CenterVertically),
                onClick = {
                    println("Go to Profile Setting")
                    onMyProfileClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Menu",
                    tint = Color.Black
                )
            }
        }
        Text(text = "Home Page", fontSize = 24.sp)
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun ContentScreen(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    onMyProfileClick: () -> Unit,
    onPlaylistClick: () -> Unit = {},
    onSongClick: (Int, String) -> Unit = { _, _ -> }

) {
    val context = LocalContext.current
    val songViewModel = SongViewModel(context.applicationContext as Application)
    val songs = songViewModel.songs
    val playlistViewModel: PlaylistViewModel = hiltViewModel()
    val libraryViewModel: LibraryViewModel = hiltViewModel()
    when(selectedIndex) {
        0 -> HomePage(
            modifier,
            onMyProfileClick = onMyProfileClick,
            onPlaylistClick = onPlaylistClick
        )
        1 -> LibraryScreen(
            libraryViewModel = libraryViewModel,
            playlistViewModel = playlistViewModel,
            songs = songs,
            onNavigateToPlaylist = onPlaylistClick,
            modifier = modifier
        )
        2 -> {
            PlaylistScreen(
                modifier = modifier,
                viewModel = playlistViewModel,
                onNavigateToSongs = { playlist ->
                    onSongClick(playlist.playlistId, playlist.title)
                }
            )
        }
    }
}