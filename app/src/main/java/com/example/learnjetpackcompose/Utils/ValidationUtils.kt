package com.example.learnjetpackcompose.Utils

import com.example.learnjetpackcompose.model.UserManager

object ValidationUtils {

    fun validateUsername(username: String): String? {
        return when {
            username.isBlank() -> "Invalid format"
            username.contains(' ') || username.contains('\t') -> "Invalid format"
            !username.all { it.isLetterOrDigit() || it == '_' || it == '-' } -> "Invalid format"
            UserManager.isUsernameExist(username) -> "Invalid format"
            else -> null
        }
    }

    fun validatePassword(password: String): String? {
        return when {
            password.isBlank() -> "Invalid format"
            password.length < 6 -> "Invalid format"
            !password.any { it.isLowerCase() } -> "Invalid format"
            !password.any { it.isUpperCase() } -> "Invalid format"
            !password.any { it.isDigit() } -> "Invalid format"
            else -> null
        }
    }

    fun validateConfirmPassword(password: String, confirmPassword: String): String? {
        return when {
            confirmPassword.isBlank() -> "Confirm password does not match"
            password != confirmPassword -> "Confirm password does not match"
            else -> null
        }
    }

    fun validateEmail(email: String): String? {
        val emailPattern = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        return when {
            email.isBlank() -> "Invalid format"
            !email.matches(emailPattern.toRegex()) -> "Invalid format"
            !email.endsWith("@apero.vn") -> "Invalid format"
            UserManager.isEmailExist(email) -> "Invalid format"
            else -> null
        }
    }
}