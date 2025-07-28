package com.example.learnjetpackcompose.Screen.SignUp

import androidx.lifecycle.ViewModel
import com.example.learnjetpackcompose.model.User
import com.example.learnjetpackcompose.model.UserManager
import com.example.learnjetpackcompose.Utils.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class SignUpViewModel : ViewModel() {

    // Input state
    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    // Error state
    private val _userNameError = MutableStateFlow<String?>(null)
    val userNameError: StateFlow<String?> = _userNameError

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError

    private val _confirmPasswordError = MutableStateFlow<String?>(null)
    val confirmPasswordError: StateFlow<String?> = _confirmPasswordError

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError

    private val _showPassword = MutableStateFlow(false)
    val showPassword: StateFlow<Boolean> = _showPassword

    private val _showConfirmPassword = MutableStateFlow(false)
    val showConfirmPassword: StateFlow<Boolean> = _showConfirmPassword

    // Feedback message
    private val _signUpMessage = MutableStateFlow<String?>(null)
    val signUpMessage: StateFlow<String?> = _signUpMessage

    private val _signUpSuccess = MutableStateFlow(false)
    val signUpSuccess: StateFlow<Boolean> = _signUpSuccess

    // Input change handlers
    fun onUserNameChange(value: String) {
        _userName.value = value
        _userNameError.value = null
    }

    fun onPasswordChange(value: String) {
        _password.value = value
        _passwordError.value = null
    }

    fun onConfirmPasswordChange(value: String) {
        _confirmPassword.value = value
        _confirmPasswordError.value = null
    }

    fun onEmailChange(value: String) {
        _email.value = value
        _emailError.value = null
    }


    fun toggleShowPassword() {
        _showPassword.value = !_showPassword.value
    }

    fun toggleShowConfirmPassword() {
        _showConfirmPassword.value = !_showConfirmPassword.value
    }


    // Validation and sign up logic
    fun validateAndSignUp(onSuccess: () -> Unit) {
        _userNameError.value = ValidationUtils.validateUsername(_userName.value)
        _passwordError.value = ValidationUtils.validatePassword(_password.value)
        _confirmPasswordError.value =
            ValidationUtils.validateConfirmPassword(_password.value, _confirmPassword.value)
        _emailError.value = ValidationUtils.validateEmail(_email.value)

        val isValid = listOf(
            _userNameError.value,
            _passwordError.value,
            _confirmPasswordError.value,
            _emailError.value
        ).all { it == null }

        if (isValid) {
            val newUser = User(_userName.value, _email.value, _password.value)
            val success = UserManager.addUser(newUser)

            if (success) {
                _signUpMessage.value = "Đăng ký thành công!"
                _signUpSuccess.value = true
                resetFields()
                onSuccess()
            } else {
                _signUpMessage.value = "Đăng ký thất bại! Username hoặc Email đã tồn tại."
                _signUpSuccess.value = false
            }
        }
    }

    private fun resetFields() {
        _userName.value = ""
        _password.value = ""
        _confirmPassword.value = ""
        _email.value = ""
    }
}
