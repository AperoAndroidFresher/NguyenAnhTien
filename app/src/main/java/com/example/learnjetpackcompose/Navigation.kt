package com.example.learnjetpackcompose

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import com.example.learnjetpackcompose.Screen.HomeScreen
import com.example.learnjetpackcompose.Screen.Library.LibraryScreen
import com.example.learnjetpackcompose.Screen.Library.LibraryViewModel
import com.example.learnjetpackcompose.Screen.Login.LoginScreen
import com.example.learnjetpackcompose.Screen.Login.LoginViewModel
import com.example.learnjetpackcompose.Screen.Login.SplashScreen
import com.example.learnjetpackcompose.Screen.Playlist.PlaylistScreen
import com.example.learnjetpackcompose.Screen.Playlist.PlaylistViewModel
import com.example.learnjetpackcompose.Screen.Playlist.Song.SongScreen
import com.example.learnjetpackcompose.Screen.SignUp.SignUpScreen
import com.example.learnjetpackcompose.Screen.SignUp.SignUpViewModel
import com.example.learnjetpackcompose.Screen.Profile.MainProfileScreen
import com.example.learnjetpackcompose.model.Song
import com.example.learnjetpackcompose.model.SongViewModel
import com.example.learnjetpackcompose.ViewModelProvider

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun NavigationApp(){

    val backStack = rememberNavBackStack(SplashNavKey)
    val loginViewModel = remember { LoginViewModel() }
    val signUpViewModel = remember { SignUpViewModel() }

    val context = LocalContext.current
    val songViewModel = SongViewModel(context.applicationContext as Application)
    val songs = songViewModel.songs

    NavDisplay(
        backStack = backStack,
        onBack = {backStack.removeLastOrNull()},
        entryProvider = entryProvider{
            entry<SplashNavKey>{key ->
                SplashScreen(onTimeout = {
                    backStack.add(LoginNavKey())
                })
            }

            entry<LoginNavKey>{key ->
                LoginScreen(
                    viewModel = loginViewModel,
                    onLoginSuccess = {
                        backStack.clear()
                        backStack.add(HomeNavKey)
                    },
                    onSignUpClick = { backStack.add(SignUpNavKey())}
                )
            }

            entry<SignUpNavKey>{key ->
                SignUpScreen(
                    viewModel = signUpViewModel,
                    onBackClick = { backStack.removeLastOrNull() },
                    onSignUpClick = { backStack.add(LoginNavKey())}
                )
            }

            entry<HomeNavKey>{key ->
                HomeScreen(
                    onMyProfileClick = { backStack.add(ProfileNavKey) },
                    onPlaylistClick = { backStack.add(PlaylistNavKey) }, // Thêm navigation đến playlist
                    onSongClick = { playlistId, playlistTitle ->
                        backStack.add(SongNavKey(playlistId, playlistTitle))
                    }
                )
            }

            entry<ProfileNavKey>{key ->
                MainProfileScreen()
            }

            entry<PlaylistNavKey>{key ->
                PlaylistScreen(
                    viewModel = ViewModelProvider.playlistViewModel,
                    onNavigateToSongs = { playlist ->
                        backStack.add(SongNavKey(
                            playlistId = playlist.id,
                            playlistTitle = playlist.title
                        ))
                    }
                )
            }

            entry<LibraryNavKey>{key ->
                LibraryScreen(
                    libraryViewModel = ViewModelProvider.libraryViewModel,
                    playlistViewModel = ViewModelProvider.playlistViewModel,
                    songs = songs,
                    onNavigateToPlaylist = {
                        backStack.add(PlaylistNavKey)
                    }
                )
            }

            entry<SongNavKey>{key ->
                SongScreen(
                    playlistId = key.playlistId,
                    playlistTitle = key.playlistTitle,
                    onBackClick = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}