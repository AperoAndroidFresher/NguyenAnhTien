package com.example.learnjetpackcompose

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class LoginNavKey(
    var userName: String = "",
    var password: String = ""
) : NavKey

@Serializable
data class SignUpNavKey(
    var userName: String = "",
    var password: String = "",
    var confirmPassword: String = "",
    var email: String = ""
) : NavKey

@Serializable
object SplashNavKey : NavKey

@Serializable
object HomeNavKey : NavKey

@Serializable
object ProfileNavKey : NavKey

@Serializable
object PlaylistNavKey : NavKey

@Serializable
object LibraryNavKey : NavKey

@Serializable
data class SongNavKey(
    val playlistId: Int,
    val playlistTitle: String
) : NavKey

@Serializable
data class PlayerNavKey(
    val songId: Long,
    val title: String,
    val artist: String,
    val duration: String,
    val data: String,
    val albumArt: String?
) : NavKey

@Serializable
object SettingNavKey: NavKey

@Serializable
object TopAlbumsNavKey: NavKey

@Serializable
object TopTracksNavKey: NavKey

@Serializable
object TopArtistNavKey: NavKey