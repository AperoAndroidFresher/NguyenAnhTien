package com.example.learnjetpackcompose.Screen.SignUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnjetpackcompose.RoomDB.Entity.User
import com.example.learnjetpackcompose.model.UserManager
import com.example.learnjetpackcompose.Utils.ValidationUtils
import com.example.learnjetpackcompose.data.repository.IUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: IUserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpState())
    val state = _state.asStateFlow()

    private val _effect = Channel<SignUpEffect>()
    val effect = _effect.receiveAsFlow()

    fun processIntent(intent: SignUpIntent) {
        when (intent) {
            is SignUpIntent.ConfirmPasswordChanged -> {
                _state.update {
                    it.copy(confirmPassword = intent.confirmPassword) }
            }

            SignUpIntent.ShowConfirmPassword -> {
                _state.update {
                    it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
            }

            is SignUpIntent.EmailChanged -> {
                _state.update {
                    it.copy(email = intent.email) }
            }

            is SignUpIntent.PasswordChanged -> {
                _state.update {
                    it.copy(password = intent.password) }
            }

            SignUpIntent.ShowPassword -> {
                _state.update {
                    it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }

            SignUpIntent.SignUpClicked -> {
                validateAndSignUp()
            }

            is SignUpIntent.UsernameChanged -> {
                _state.update {
                    it.copy(username = intent.username) }
            }
        }
    }

    // Validation and sign up logic
    private fun validateAndSignUp() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val currentState = _state.value
            var usernameError = ValidationUtils.validateUsername(currentState.username)
            var emailError = ValidationUtils.validateEmail(currentState.email)

            if (usernameError == null && userRepository.getUserByUsername(currentState.username) != null) {
                usernameError = "Username already exists"
            }
            if (emailError == null && userRepository.getUserByEmail(currentState.email) != null) {
                emailError = "Email already exists"
            }
            val validationErrors = SignUpErrors(
                usernameError = ValidationUtils.validateUsername(currentState.username),
                emailError = ValidationUtils.validateEmail(currentState.email),
                passwordError = ValidationUtils.validatePassword(currentState.password),
                confirmPasswordError = ValidationUtils.validateConfirmPassword(
                    currentState.password,
                    currentState.confirmPassword
                )
            )

            val isValid = with(validationErrors) {
                usernameError == null && emailError == null && passwordError == null && confirmPasswordError == null
            }
            if (isValid) {
                val newUser = User(username = currentState.username, email = currentState.email, password = currentState.password
                )
                val success = UserManager.addUser(newUser)

                if (success) {
                    userRepository.insertUser(newUser)
                    _effect.send(SignUpEffect.ShowMessage("Dang ky thanh cong"))
                    _effect.send(SignUpEffect.NavigateToLogin)
                    _state.value = SignUpState()
                } else {
                    _effect.send(SignUpEffect.ShowMessage("Dang ky that bai"))
                    _state.update { it.copy(isLoading = false) }
                }
            } else {
                _state.value = SignUpState()
                _state.update { it.copy(isLoading = false, errors = validationErrors) }
            }
        }
    }
}

