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
import com.example.learnjetpackcompose.Screen.HomeScreen
import com.example.learnjetpackcompose.Screen.Library.LibraryScreen
import com.example.learnjetpackcompose.Screen.Login.LoginScreen
import com.example.learnjetpackcompose.Screen.Login.LoginViewModel
import com.example.learnjetpackcompose.Screen.Login.SplashScreen
import com.example.learnjetpackcompose.Screen.Playlist.PlaylistScreen
import com.example.learnjetpackcompose.Screen.Playlist.Song.SongScreen
import com.example.learnjetpackcompose.Screen.SignUp.SignUpScreen
import com.example.learnjetpackcompose.Screen.Profile.MainProfileScreen
import com.example.learnjetpackcompose.model.UserManager

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
                HomeScreen(
                    onMyProfileClick = { backStack.add(ProfileNavKey) },
                    onPlaylistClick = { backStack.add(PlaylistNavKey) },
                    onSongClick = { playlistId, playlistTitle ->
                        backStack.add(SongNavKey(playlistId, playlistTitle))
                    }
                )
            }

            entry<ProfileNavKey> { key ->
                MainProfileScreen()
            }

            entry<PlaylistNavKey> { key ->
                PlaylistScreen(
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

            entry<LibraryNavKey> { key ->
                LibraryScreen(
                    libraryViewModel = hiltViewModel(),
                    playlistViewModel = hiltViewModel(),
                    songs = songs,
                    onNavigateToPlaylist = {
                        backStack.add(PlaylistNavKey)
                    }
                )
            }

            entry<SongNavKey> { key ->
                SongScreen(
                    playlistId = key.playlistId,
                    playlistTitle = key.playlistTitle,
                    onBackClick = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}