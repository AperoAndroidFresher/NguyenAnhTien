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
object HomeNavKey : NavKey

@Serializable
object ProfileNavKey : NavKey

@Serializable
object PlaylistNavKey : NavKey