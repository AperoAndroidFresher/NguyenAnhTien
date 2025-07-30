package com.example.learnjetpackcompose.Screen.SignUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnjetpackcompose.model.User
import com.example.learnjetpackcompose.model.UserManager
import com.example.learnjetpackcompose.Utils.ValidationUtils
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {

    private val _state = MutableStateFlow(SignUpState())
    val state = _state.asStateFlow()

    private val _effect = Channel<SignUpEffect>()
    val effect = _effect.receiveAsFlow()

    fun processIntent(intent: SignUpIntent) {
        when (intent) {
            is SignUpIntent.ConfirmPasswordChanged -> {
                _state.update { it.copy(confirmPassword = intent.confirmPassword) }
            }

            SignUpIntent.ShowConfirmPassword -> {
                _state.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
            }

            is SignUpIntent.EmailChanged -> {
                _state.update { it.copy(email = intent.email) }
            }

            is SignUpIntent.PasswordChanged -> {
                _state.update { it.copy(password = intent.password) }
            }

            SignUpIntent.ShowPassword -> {
                _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }

            SignUpIntent.SignUpClicked -> {
                validateAndSignUp()
            }

            is SignUpIntent.UsernameChanged -> {
                _state.update { it.copy(username = intent.username) }
            }
        }
    }

    // Validation and sign up logic
    private fun validateAndSignUp() {
        viewModelScope.launch{
            _state.update{it.copy(isLoading = true)}

            val currentState = _state.value

            val validationErrors = SignUpErrors(
                usernameError = ValidationUtils.validateUsername(currentState.username),
                emailError = ValidationUtils.validateEmail(currentState.email),
                passwordError = ValidationUtils.validatePassword(currentState.password),
                confirmPasswordError = ValidationUtils.validateConfirmPassword(currentState.password, currentState.confirmPassword)
            )
            val isValid = with(validationErrors){
                usernameError == null && emailError == null && passwordError == null && confirmPasswordError == null
            }
            if(isValid){
                val newUser = User(currentState.username, currentState.email, currentState.password)
                val success = UserManager.addUser(newUser)

                if(success){
                    _effect.send(SignUpEffect.ShowMessage("Dang ky thanh cong"))
                    _state.value = SignUpState()
                    _effect.send(SignUpEffect.NavigateToLogin)
                }else{
                    _effect.send(SignUpEffect.ShowMessage("Dang ky that bai"))
                    _state.update{it.copy(isLoading = false)}
                }
            }else{
                _state.value = SignUpState()
                _state.update{it.copy(isLoading = false, errors = validationErrors)}
            }
        }
    }


}

