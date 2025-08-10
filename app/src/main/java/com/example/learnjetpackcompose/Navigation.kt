package com.example.learnjetpackcompose

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import com.example.learnjetpackcompose.RoomDB.Entity.SongViewModel
import com.example.learnjetpackcompose.Screen.Home.HomeScreen
import com.example.learnjetpackcompose.Screen.Library.LibraryScreen
import com.example.learnjetpackcompose.Screen.Login.LoginScreen
import com.example.learnjetpackcompose.Screen.Login.LoginViewModel
import com.example.learnjetpackcompose.Screen.Login.SplashScreen
import com.example.learnjetpackcompose.Screen.Playlist.PlaylistScreen
import androidx.compose.material3.Scaffold
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.painterResource
import com.example.learnjetpackcompose.data.model.NavBottomItems
import com.example.learnjetpackcompose.Screen.Playlist.Song.SongScreen
import com.example.learnjetpackcompose.Screen.SignUp.SignUpScreen
import com.example.learnjetpackcompose.Screen.Profile.MainProfileScreen
import com.example.learnjetpackcompose.Screen.Player.PlayerScreen
import com.example.learnjetpackcompose.data.model.UserManager
import com.example.learnjetpackcompose.data.model.PlaybackManager
import com.example.learnjetpackcompose.Screen.Player.PlayerBar
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.Column
import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import com.example.learnjetpackcompose.Screen.Player.PlayerViewModel

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun NavigationApp() {
    val context = LocalContext.current
    val backStack = rememberNavBackStack(SplashNavKey)

    val songViewModel = SongViewModel(context.applicationContext as Application)
    val songs = songViewModel.songs

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<SplashNavKey> { key ->
                val viewModel = hiltViewModel<LoginViewModel>()
                SplashScreen(onTimeout = {
                    backStack.clear()
                    if (viewModel.isLoggedIn()) {
                        viewModel.getLoggedInUserId()?.let { userId ->
                            UserManager.setCurrentUserId(userId.toInt())
                        }
                        backStack.add(HomeNavKey)
                    } else {
                        backStack.add(LoginNavKey())
                    }
                })
            }

            entry<LoginNavKey> { key ->
                LoginScreen(
                    viewModel = hiltViewModel(),
                    onLoginSuccess = {
                        backStack.clear()
                        backStack.add(HomeNavKey)
                    },
                    onSignUpClick = { backStack.add(SignUpNavKey()) }
                )
            }

            entry<SignUpNavKey> { key ->
                SignUpScreen(
                    viewModel = hiltViewModel(),
                    onBackClick = { backStack.removeLastOrNull() },
                    onSignUpClick = { backStack.add(LoginNavKey()) }
                )
            }

            entry<HomeNavKey> { key ->
                AppShell(
                    currentIndex = 0,
                    onNavigate = { idx ->
                        when (idx) {
                            0 -> { backStack.add(HomeNavKey) }
                            1 -> { backStack.add(LibraryNavKey) }
                            2 -> { backStack.add(PlaylistNavKey) }
                        }
                    },
                    backStack = backStack
                ) { innerPadding ->
                    HomeScreen(
                        modifier = Modifier.padding(innerPadding),
                        onMyProfileClick = { backStack.add(ProfileNavKey) },
                        onPlaylistClick = { backStack.add(PlaylistNavKey) },
                        onSongClick = { playlistId, playlistTitle ->
                            backStack.add(SongNavKey(playlistId, playlistTitle))
                        }
                    )
                }
            }

            entry<ProfileNavKey> { key ->
                MainProfileScreen()
            }

            entry<PlaylistNavKey> { key ->
                AppShell(
                    currentIndex = 2,
                    onNavigate = { idx ->
                        when (idx) {
                            0 -> { backStack.add(HomeNavKey) }
                            1 -> { backStack.add(LibraryNavKey) }
                            2 -> { backStack.add(PlaylistNavKey) }
                        }
                    },
                    backStack = backStack
                ) { innerPadding ->
                    PlaylistScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = hiltViewModel(),
                        onNavigateToSongs = { playlist ->
                            backStack.add(
                                SongNavKey(
                                    playlistId = playlist.playlistId,
                                    playlistTitle = playlist.title
                                )
                            )
                        }
                    )
                }
            }

            entry<LibraryNavKey> { key ->
                AppShell(
                    currentIndex = 1,
                    onNavigate = { idx ->
                        when (idx) {
                            0 -> { backStack.add(HomeNavKey) }
                            1 -> { backStack.add(LibraryNavKey) }
                            2 -> { backStack.add(PlaylistNavKey) }
                        }
                    },
                    backStack = backStack
                ) { innerPadding ->
                    LibraryScreen(
                        libraryViewModel = hiltViewModel(),
                        playlistViewModel = hiltViewModel(),
                        songs = songs,
                        onNavigateToPlaylist = {
                            backStack.add(PlaylistNavKey)
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }

            entry<SongNavKey> { key ->
                AppShell(
                    currentIndex = 2,
                    onNavigate = { idx ->
                        when (idx) {
//                            0 -> { backStack.clear(); backStack.add(HomeNavKey) }
                            0 -> { backStack.add(HomeNavKey) }
                            1 -> { backStack.add(LibraryNavKey) }
                            2 -> { backStack.add(PlaylistNavKey) }
                        }
                    },
                    backStack = backStack
                ) { innerPadding ->
                    SongScreen(
                        playlistId = key.playlistId,
                        playlistTitle = key.playlistTitle,
                        onBackClick = { backStack.removeLastOrNull() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }

            entry<PlayerNavKey> { key ->
                val playerViewModel: PlayerViewModel = hiltViewModel()

                PlayerScreen(
                    songTitle = key.title,
                    songArtist = key.artist,
                    songDuration = key.duration,
                    albumArtUrl = key.albumArt,
                    onBackClick = { backStack.removeLastOrNull() },
                    onExitClick = {
                        // Return to home screen
//                        backStack.clear()
                        backStack.add(HomeNavKey)
                    },
                    viewModel = playerViewModel
                )
            }
        }
    )
}

@Composable
private fun AppShell(
    currentIndex: Int,
    onNavigate: (Int) -> Unit,
    backStack: androidx.navigation3.runtime.NavBackStack,
    content: @Composable (PaddingValues) -> Unit
) {
    val navItemsList = listOf(
        NavBottomItems("Home", R.drawable.icon_home),
        NavBottomItems("Library", R.drawable.icon_library),
        NavBottomItems("My Playlist", R.drawable.icon_playlist)
    )
    var selectedIndex by remember { mutableStateOf(currentIndex) }

    val currentSong by PlaybackManager.currentSong.collectAsState()
    val isPlaying by PlaybackManager.isPlaying.collectAsState()
    val playerViewModel: PlayerViewModel = hiltViewModel()
    Scaffold(
        bottomBar = {
            Column {
                // PlayerBar được đặt phía trên bottomBar
                if (currentSong != null) {
                    Log.d("PlayerBar", "Displaying: '${currentSong!!.title}', Duration: '${currentSong!!.duration}'")
                    PlayerBar(
                        title = currentSong!!.title,
                        duration = currentSong!!.duration,
                        isPlaying = isPlaying,
                        onPlayPauseClick = {
                            playerViewModel.togglePlayPause()
                        },
                        onPlayerBarClick = {
                            // Navigate to PlayerScreen với thông tin bài hát hiện tại
                            val playerNavKey = PlayerNavKey(
                                songId = currentSong!!.songId,
                                title = currentSong!!.title,
                                artist = currentSong!!.artist,
                                duration = currentSong!!.duration,
                                data = currentSong!!.data,
                                albumArt = currentSong!!.albumArt
                            )
                            backStack.add(playerNavKey)
                        },
                        modifier = Modifier,
                        viewModel = playerViewModel
                    )
                }

                // NavigationBar ở phía dưới
                NavigationBar {
                    navItemsList.forEachIndexed { index, navItem ->
                        NavigationBarItem(
                            selected = selectedIndex == index,
                            onClick = {
                                selectedIndex = index
                                onNavigate(index)
                            },
                            icon = { Icon(painterResource(navItem.icon), contentDescription = navItem.label) },
                            label = { Text(navItem.label, style = MaterialTheme.typography.labelMedium) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}