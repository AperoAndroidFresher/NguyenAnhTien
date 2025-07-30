package com.example.learnjetpackcompose.Screen.SignUp

data class SignUpState(
    val username: String = "",
    val password: String = "",
    val confirmPassword: String ="",
    val email: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errors: SignUpErrors = SignUpErrors()
)

data class SignUpErrors(
    val usernameError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val emailError: String? = null
)
sealed interface SignUpIntent{
    data class UsernameChanged(val username: String): SignUpIntent
    data class PasswordChanged(val password: String): SignUpIntent
    data class ConfirmPasswordChanged(val confirmPassword: String): SignUpIntent
    data class EmailChanged(val email: String): SignUpIntent
    object SignUpClicked: SignUpIntent
    object ShowPassword: SignUpIntent
    object ShowConfirmPassword: SignUpIntent
}

sealed interface SignUpEffect{
    data object NavigateToLogin: SignUpEffect
    data class ShowMessage(val message: String): SignUpEffect
}