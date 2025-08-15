package com.example.learnjetpackcompose.Screen.SignUp

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import com.example.learnjetpackcompose.Component.ImageContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnjetpackcompose.Component.IconButtonCustom
import com.example.learnjetpackcompose.Component.InputTextField
import com.example.learnjetpackcompose.Component.PasswordTextField
import com.example.learnjetpackcompose.R

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    onSignUpClick: () -> Unit,
    onBackClick: () -> Unit = {}
) {

    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) {
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
            .padding(12.dp)
            .verticalScroll(scrollState)
    ) {
        IconButtonCustom(
            onClick = { onBackClick() },R.drawable.icon_back, "Back"
        )
        ImageContent(painterResource(R.drawable.aperologo), "Sign Up")

        InputTextField(
            value = state.username,
            onValueChange = { viewModel.processIntent(SignUpIntent.UsernameChanged(it)) },
            label = "UserName",
            leadingIcon = painterResource(R.drawable.icon_user),
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
        PasswordTextField(
            value = state.password,
            onValueChange = { viewModel.processIntent(SignUpIntent.PasswordChanged(it)) },
            isPasswordVisible = state.isPasswordVisible,
            onShowPassword = { viewModel.processIntent(SignUpIntent.ShowPassword) },
            label = "Password",
            modifier = Modifier.fillMaxWidth()
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
        PasswordTextField(
            value = state.confirmPassword,
            onValueChange = { viewModel.processIntent(SignUpIntent.ConfirmPasswordChanged(it)) },
            isPasswordVisible = state.isConfirmPasswordVisible,
            onShowPassword = { viewModel.processIntent(SignUpIntent.ShowConfirmPassword) },
            label = "Confirm Password",
            modifier = Modifier.fillMaxWidth()
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
        InputTextField(
            value = state.email,
            onValueChange = { viewModel.processIntent(SignUpIntent.EmailChanged(it)) },
            label = "Email",
            leadingIcon = painterResource(R.drawable.icon_email),
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
                .padding(bottom = 36.dp)
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


