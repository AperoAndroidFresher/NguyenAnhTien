package com.example.learnjetpackcompose.Screen.Login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnjetpackcompose.data.model.UserManager
import com.example.learnjetpackcompose.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _effect = Channel<LoginEffect>()
    val effect = _effect.receiveAsFlow()

    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun processIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.UsernameChanged -> {
                _state.update { it.copy(username = intent.username, error = null) }
            }

            is LoginIntent.PasswordChanged -> {
                _state.update { it.copy(password = intent.password, error = null) }
            }

            is LoginIntent.RememberMeChanged -> {
                _state.update { it.copy(rememberMe = intent.isChecked) }
            }

            is LoginIntent.ShowPasswordVisibility -> {
                _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }

            is LoginIntent.LoginClick -> {
                login()
            }
        }
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("is_logged_in", false)
    }

    fun getLoggedInUserId(): String? {
        return sharedPreferences.getString("user_id", null)
    }

    private fun login() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val currentState = _state.value
            val user = userRepository.getUserByUsername(currentState.username)
            if (user != null && user.password == currentState.password) {
                UserManager.setCurrentUserId(user.userId)
                with(sharedPreferences.edit()) {
                    putBoolean("is_logged_in", true)
                    putString("user_id", user.userId.toString())
                    apply()
                }
                _effect.send(LoginEffect.NavigateToHome)
            } else {
                _state.update { it.copy(isLoading = false, error = "Invalid username or password") }
            }
        }
    }

}