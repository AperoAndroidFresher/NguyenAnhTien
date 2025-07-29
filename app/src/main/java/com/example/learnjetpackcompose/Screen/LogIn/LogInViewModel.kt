package com.example.learnjetpackcompose.Screen.LogIn

import androidx.lifecycle.ViewModel
import com.example.learnjetpackcompose.model.UserManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel : ViewModel() {

    // State Manager
    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _showPassword = MutableStateFlow(false)
    val showPassword: StateFlow<Boolean> = _showPassword


    private val _rememberMe = MutableStateFlow(false)
    val rememberMe: StateFlow<Boolean> = _rememberMe

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    private val _showLoginError = MutableStateFlow(false)
    val showLoginError: StateFlow<Boolean> = _showLoginError

    fun onUserNameChange(newUserName: String) {
        _userName.value = newUserName
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun onRememberMeChange(checked: Boolean) {
        _rememberMe.value = checked
    }

    fun toggleShowPassword() {
        _showPassword.value = !_showPassword.value
    }

    fun onLoginClick() {

        val currentUsername = _userName.value
        val currentPassword = _password.value


        if (UserManager.isUserExist(currentUsername, currentPassword)) {
            _loginSuccess.value = true
            _showLoginError.value = false
        } else {
            _loginSuccess.value = false
            _showLoginError.value = true
        }
    }
    fun resetLoginState() {
        _loginSuccess.value = false
        _showLoginError.value = false
    }

}
