package com.example.learnjetpackcompose

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import com.example.learnjetpackcompose.Screen.Home.HomeScreen
import com.example.learnjetpackcompose.Screen.Library.LibraryScreen
import com.example.learnjetpackcompose.Screen.Library.LibraryIntent
import com.example.learnjetpackcompose.Screen.Library.LibraryViewModel
import com.example.learnjetpackcompose.Screen.Login.LoginScreen
import com.example.learnjetpackcompose.Screen.Login.LoginViewModel
import com.example.learnjetpackcompose.Screen.Login.SplashScreen
import com.example.learnjetpackcompose.Screen.Playlist.PlaylistScreen
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.learnjetpackcompose.Screen.Playlist.Song.SongScreen
import com.example.learnjetpackcompose.Screen.SignUp.SignUpScreen
import com.example.learnjetpackcompose.Screen.Profile.MainProfileScreen
import com.example.learnjetpackcompose.Screen.Player.PlayerScreen
import com.example.learnjetpackcompose.data.model.UserManager
import com.example.learnjetpackcompose.data.model.PlaybackManager
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import com.example.learnjetpackcompose.Component.AppNavigationBottomBar
import com.example.learnjetpackcompose.Screen.Home.Component.TopAlbumsScreen
import com.example.learnjetpackcompose.Screen.Home.Component.TopArtistScreen
import com.example.learnjetpackcompose.Screen.Home.Component.TopTracksScreen
import com.example.learnjetpackcompose.Screen.Home.Setting.SettingScreen
import com.example.learnjetpackcompose.Screen.Player.Component.PlayerBar
import com.example.learnjetpackcompose.Screen.Player.PlayerViewModel
import com.example.learnjetpackcompose.Screen.Playlist.Song.Component.SongSorted
import com.example.learnjetpackcompose.Screen.Profile.ProfileIntent
import com.example.learnjetpackcompose.Screen.Profile.ProfileViewModel

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun NavigationApp() {
    val context = LocalContext.current
    val backStack = rememberNavBackStack(SplashNavKey)
    val playerVM: PlayerViewModel = hiltViewModel()
    val profileVM : ProfileViewModel = hiltViewModel()

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
                        onSettingClick = {playerVM.stopPreview(); backStack.add(SettingNavKey)},
                        onMyProfileClick = { playerVM.stopPreview();backStack.add(ProfileNavKey) },
                        onAlbumClick = {backStack.add(TopAlbumsNavKey)},
                        onTrackClick = {backStack.add(TopTracksNavKey)},
                        onArtistClick = {backStack.add(TopArtistNavKey)}
                    )
                }
            }

            entry<ProfileNavKey> { key ->
                MainProfileScreen(
                    viewModel = hiltViewModel(),
                    onLogout = {
                        profileVM.processIntent(ProfileIntent.Logout)
                    },
                    navigateToLogin = {
                        backStack.clear()
                        backStack.add(LoginNavKey())
                    }
                )
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
                    val activity = context as MainActivity
                    val libraryViewModel = hiltViewModel<LibraryViewModel>()
                    LibraryScreen(
                        libraryViewModel = libraryViewModel,
                        playlistViewModel = hiltViewModel(),
                        onNavigateToPlaylist = {
                            backStack.add(PlaylistNavKey)
                        },
                        onRequestPermission = {
                            activity.requestPermission {
                                libraryViewModel.refreshLocalSongs()
                                libraryViewModel.processIntent(LibraryIntent.OnPermissionGranted)
                            }
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
                        onSortClick = {
                            backStack.add(
                                SongSortedNavKey(
                                    playlistId = key.playlistId,
                                )
                            )
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }

            entry<PlayerNavKey> { key ->
                val playerViewModel: PlayerViewModel = hiltViewModel()
                PlayerScreen(
                    onBackClick = { backStack.removeLastOrNull() },
                    onExitClick = {
                        backStack.add(HomeNavKey)
                    },
                    viewModel = playerViewModel
                )
            }
            entry<SettingNavKey>{key ->
                SettingScreen(
                    onBack = {backStack.removeLastOrNull()},
                    onSave = {backStack.add(HomeNavKey)}
                )
            }

            entry(TopAlbumsNavKey){key ->
                TopAlbumsScreen(
                    onBack = {backStack.removeLastOrNull()}
                )
            }
            entry(TopTracksNavKey){key ->
                TopTracksScreen(
                    onBack = {backStack.removeLastOrNull()}
                )
            }
            entry(TopArtistNavKey){key ->
                TopArtistScreen(
                    onBack = {backStack.removeLastOrNull()}
                )
            }
            entry<SongSortedNavKey>{ key ->
                SongSorted(
                    playlistId = key.playlistId,
                    songs = hiltViewModel<com.example.learnjetpackcompose.Screen.Playlist.PlaylistViewModel>().state.value.playlists
                        .find { it.playlistId == key.playlistId }?.songs ?: emptyList(),
                    onBackClick = { backStack.removeLastOrNull() },
                    onSaved = { backStack.removeLastOrNull() }
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

    var selectedIndex by remember { mutableStateOf(currentIndex) }

    val currentSong by PlaybackManager.currentSong.collectAsState()
    val isPlaying by PlaybackManager.isPlaying.collectAsState()
    val playerViewModel: PlayerViewModel = hiltViewModel()
    Scaffold(
        bottomBar = {
            Column {
                if (currentSong != null) {
                    PlayerBar(
                        title = currentSong!!.title,
                        duration = currentSong!!.duration,
                        isPlaying = isPlaying,
                        onPlayPauseClick = {
                            playerViewModel.togglePlayPause()
                        },
                        onPlayerBarClick = {
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

                AppNavigationBottomBar(selectedIndex, onNavigate)
            }
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}