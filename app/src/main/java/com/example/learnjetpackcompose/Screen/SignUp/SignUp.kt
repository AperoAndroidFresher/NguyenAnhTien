package com.example.learnjetpackcompose.Screen.SignUp

import android.widget.Toast
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnjetpackcompose.R


@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    onSignUpClick: () -> Unit,
    onBackClick: () -> Unit = {}
) {

    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect (Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                // Handle effects here
                is SignUpEffect.NavigateToLogin -> {
                    onSignUpClick()
                }
                is SignUpEffect.ShowMessage -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
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
            value = state.username,
            onValueChange = {
                viewModel.processIntent(SignUpIntent.UsernameChanged(it))
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
            isError = state.errors.usernameError != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = if (state.errors.usernameError != null) Color.Red else Color(0xFF06A0B5),
                unfocusedBorderColor = if (state.errors.usernameError != null) Color.Red else Color.White.copy(0.5f),
                errorBorderColor = Color.Red
            )
        )
        if (state.errors.usernameError != null) {
            Text(
                text = state.errors.usernameError!!,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))


        OutlinedTextField(
            value = state.password,
            onValueChange = {
                viewModel.processIntent(SignUpIntent.PasswordChanged(it))
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
                IconButton(onClick = { viewModel.processIntent(SignUpIntent.ShowPassword)}) {
                    Icon(
                        painter = painterResource(id = R.drawable.visible),
                        contentDescription = if (state.isPasswordVisible) "Hide password" else "Show password",
                        tint = Color.White
                    )
                }
            },
            visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            isError = state.errors.passwordError != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = if (state.errors.passwordError != null) Color.Red else Color(0xFF06A0B5),
                unfocusedBorderColor = if (state.errors.passwordError != null) Color.Red else Color.White.copy(0.5f),
                errorBorderColor = Color.Red
            )
        )
        if (state.errors.passwordError != null) {
            Text(
                text = state.errors.passwordError!!,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.confirmPassword,
            onValueChange = {
                viewModel.processIntent(SignUpIntent.ConfirmPasswordChanged(it))
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
                IconButton(onClick = { viewModel.processIntent(SignUpIntent.ShowConfirmPassword) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.visible),
                        contentDescription = if (state.isConfirmPasswordVisible) "Hide password" else "Show password",
                        tint = Color.White
                    )
                }
            },
            visualTransformation = if (state.isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            isError = state.errors.confirmPasswordError != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = if (state.errors.confirmPasswordError != null) Color.Red else Color(0xFF06A0B5),
                unfocusedBorderColor = if (state.errors.confirmPasswordError != null) Color.Red else Color.White.copy(0.5f),
                errorBorderColor = Color.Red
            )
        )
        if (state.errors.confirmPasswordError != null) {
            Text(
                text = state.errors.confirmPasswordError!!,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))


        OutlinedTextField(
            value = state.email,
            onValueChange = {
                viewModel.processIntent(SignUpIntent.EmailChanged(it))
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
            isError = state.errors.emailError != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = if (state.errors.emailError != null) Color.Red else Color(0xFF06A0B5),
                unfocusedBorderColor = if (state.errors.emailError != null) Color.Red else Color.White.copy(0.5f),
                errorBorderColor = Color.Red
            )
        )
        if (state.errors.emailError != null) {
            Text(
                text = state.errors.emailError!!,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = { viewModel.processIntent(SignUpIntent.SignUpClicked) },
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

