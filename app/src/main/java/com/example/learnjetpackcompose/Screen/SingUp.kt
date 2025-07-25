package com.example.learnjetpackcompose.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnjetpackcompose.R
import com.example.learnjetpackcompose.User
import com.example.learnjetpackcompose.UserManager
import com.example.learnjetpackcompose.ValidationUtils

@Composable
fun SignUpScreen(
    userName: String,
    password: String,
    confirmPassword: String,
    email: String,
    onUserNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onSignUpClick: () -> Unit,
    onBackClick: () -> Unit = {}
) {
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var signUpMessage by remember { mutableStateOf<String?>(null) }
    var isSignUpSuccess by remember { mutableStateOf(false) }

    fun validateAndSignUp() {
        usernameError = null
        passwordError = null
        confirmPasswordError = null
        emailError = null
        signUpMessage = null
        isSignUpSuccess = false

        usernameError = ValidationUtils.validateUsername(userName)
        passwordError = ValidationUtils.validatePassword(password)
        confirmPasswordError = ValidationUtils.validateConfirmPassword(password, confirmPassword)
        emailError = ValidationUtils.validateEmail(email)

        if (usernameError == null && passwordError == null &&
            confirmPasswordError == null && emailError == null) {

            val newUser = User(userName, email, password)
            val success = UserManager.addUser(newUser)

            if (success) {
                signUpMessage = "Đăng ký thành công!"
                isSignUpSuccess = true
                onUserNameChange("")
                onPasswordChange("")
                onConfirmPasswordChange("")
                onEmailChange("")
                onBackClick()
            } else {
                signUpMessage = "Đăng ký thất bại! Username hoặc Email đã tồn tại."
                isSignUpSuccess = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState()),
    ) {
        IconButton(
            onClick = {onBackClick()},
            modifier = Modifier
                .size(40.dp)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        Image(
            painter = painterResource(id = R.drawable.aperologo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(350.dp)
                .align(Alignment.CenterHorizontally)
        )

        Text(
            text = "Sign Up",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Username Field
        OutlinedTextField(
            value = userName,
            onValueChange = {
                onUserNameChange(it)
                usernameError = null
            },
            label = { Text(text = "Username", color = Color.White.copy(0.7f)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Username Icon",
                    tint = Color.White.copy(alpha = 0.5f)
                )
            },
            isError = usernameError != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = if (usernameError != null) Color.Red else Color(0xFF06A0B5),
                unfocusedBorderColor = if (usernameError != null) Color.Red else Color.White.copy(0.5f),
                errorBorderColor = Color.Red
            )
        )
        if (usernameError != null) {
            Text(
                text = usernameError!!,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))


        OutlinedTextField(
            value = password,
            onValueChange = {
                onPasswordChange(it)
                passwordError = null
            },
            label = { Text(text = "Password", color = Color.White.copy(0.7f)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Password Icon",
                    tint = Color.White.copy(alpha = 0.5f)
                )
            },
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        painter = painterResource(id = R.drawable.visible),
                        contentDescription = if (showPassword) "Hide password" else "Show password",
                        tint = Color.White
                    )
                }
            },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            isError = passwordError != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = if (passwordError != null) Color.Red else Color(0xFF06A0B5),
                unfocusedBorderColor = if (passwordError != null) Color.Red else Color.White.copy(0.5f),
                errorBorderColor = Color.Red
            )
        )
        if (passwordError != null) {
            Text(
                text = passwordError!!,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                onConfirmPasswordChange(it)
                confirmPasswordError = null
            },
            label = { Text(text = "Confirm Password", color = Color.White.copy(0.7f)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Password Icon",
                    tint = Color.White.copy(alpha = 0.5f)
                )
            },
            trailingIcon = {
                IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                    Icon(
                        painter = painterResource(id = R.drawable.visible),
                        contentDescription = if (showConfirmPassword) "Hide password" else "Show password",
                        tint = Color.White
                    )
                }
            },
            visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
            isError = confirmPasswordError != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = if (confirmPasswordError != null) Color.Red else Color(0xFF06A0B5),
                unfocusedBorderColor = if (confirmPasswordError != null) Color.Red else Color.White.copy(0.5f),
                errorBorderColor = Color.Red
            )
        )
        if (confirmPasswordError != null) {
            Text(
                text = confirmPasswordError!!,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))


        OutlinedTextField(
            value = email,
            onValueChange = {
                onEmailChange(it)
                emailError = null
            },
            label = { Text(text = "Email", color = Color.White.copy(0.7f)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email Icon",
                    tint = Color.White.copy(alpha = 0.5f)
                )
            },
            isError = emailError != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = if (emailError != null) Color.Red else Color(0xFF06A0B5),
                unfocusedBorderColor = if (emailError != null) Color.Red else Color.White.copy(0.5f),
                errorBorderColor = Color.Red
            )
        )
        if (emailError != null) {
            Text(
                text = emailError!!,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        if (signUpMessage != null) {
            Text(
                text = signUpMessage!!,
                color = if (isSignUpSuccess) Color.Green else Color.Red,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = { validateAndSignUp() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(50.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF06A0B5),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Sign Up",
                fontSize = 20.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    SignUpScreen(
        userName = userName,
        password = password,
        confirmPassword = confirmPassword,
        email = email,
        onUserNameChange = { userName = it },
        onPasswordChange = { password = it },
        onConfirmPasswordChange = { confirmPassword = it },
        onEmailChange = { email = it },
        onSignUpClick = {}
    )
}
