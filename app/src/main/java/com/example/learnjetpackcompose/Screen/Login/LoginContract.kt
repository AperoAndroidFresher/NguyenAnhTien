package com.example.learnjetpackcompose.Screen.Login

data class LoginState(
    val username: String ="",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val rememberMe: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface LoginIntent{
    data class UsernameChanged(val username: String): LoginIntent
    data class PasswordChanged(val password: String): LoginIntent
    data class RememberMeChanged(val isChecked: Boolean): LoginIntent
    data object ShowPasswordVisibility: LoginIntent
    data object LoginClick: LoginIntent
}

sealed interface LoginEffect{
    data object NavigateToHome: LoginEffect
    data object NavigateToSignUp: LoginEffect
}