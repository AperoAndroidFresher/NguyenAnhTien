package com.example.learnjetpackcompose.Screen.Login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnjetpackcompose.model.UserManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _effect = Channel<LoginEffect>()
    val effect = _effect.receiveAsFlow()

    fun processIntent(intent: LoginIntent){
        when(intent){
            is LoginIntent.UsernameChanged ->{
                _state.update {it.copy(username = intent.username, error = null)}
            }

            is LoginIntent.PasswordChanged -> {
                _state.update{it.copy(password = intent.password, error = null)}
            }

            is LoginIntent.RememberMeChanged -> {
                _state.update {it.copy(rememberMe = intent.isChecked)}
            }

            is LoginIntent.ShowPasswordVisibility -> {
                _state.update {it.copy(isPasswordVisible = !it.isPasswordVisible)}
            }
            is LoginIntent.LoginClick -> {
                login()
            }
        }
    }

    private fun login(){
        viewModelScope.launch{
            _state.update {it.copy(isLoading = true)}

            val currentState = _state.value
            if(UserManager.isUserExist(currentState.username, currentState.password)){
                _effect.send(LoginEffect.NavigateToHome)
            } else{
                _state.update {it.copy(isLoading = false, error = "Invalid username or password")}
            }
        }
    }

}
