package com.example.learnjetpackcompose.Screen.Login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learnjetpackcompose.R
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.sp
import com.example.learnjetpackcompose.Component.ImageContent
import com.example.learnjetpackcompose.Component.InputTextField
import com.example.learnjetpackcompose.Component.MyButton
import com.example.learnjetpackcompose.Component.PasswordTextField
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit,
    onSignUpClick: () -> Unit
) {

    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is LoginEffect.NavigateToHome -> {
                    onLoginSuccess()
                }

                is LoginEffect.NavigateToSignUp -> {
                    onSignUpClick()
                }
            }

        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(scrollState)
    ) {
        ImageContent(painterResource(R.drawable.aperologo), "Log in")

        Spacer(modifier = Modifier.height(16.dp))

        InputTextField(
            value = state.username,
            onValueChange = {viewModel.processIntent(LoginIntent.UsernameChanged(it))},
            label = "UserName",
            leadingIcon = painterResource(R.drawable.icon_user),
        )

        Spacer(modifier = Modifier.height(10.dp))
        PasswordTextField(
            value = state.password,
            onValueChange = {viewModel.processIntent(LoginIntent.PasswordChanged(it))},
            isPasswordVisible = state.isPasswordVisible,
            onShowPassword = {viewModel.processIntent(LoginIntent.ShowPasswordVisibility)},
            label = "Password",
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Checkbox(
                checked = state.rememberMe,
                onCheckedChange = { viewModel.processIntent(LoginIntent.RememberMeChanged(it)) },
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )

            Text(
                text = "Remember me",
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.align(Alignment.CenterVertically),
                fontSize = 20.sp
            )
        }

        MyButton(
            onClick = {viewModel.processIntent(LoginIntent.LoginClick) },
            label = "Login",
            containerColor = Color(0xFF06A0B5),
            contentColor = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .height(50.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        BottomText(onSignUpClick)
    }
}

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.aperologo),
            contentDescription = "Logo Apero",
            modifier = Modifier.size(350.dp)
        )
        Text(
            text = "Apero Music",
            fontSize = 32.sp,
            color = Color.White
        )
    }

    LaunchedEffect(key1 = true) {
        delay(2000L)
        onTimeout()
    }
}


@Composable
fun BottomText(
    onSignUpClick: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Don't have an account?",
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 20.sp,
        )
        TextButton(
            onClick = onSignUpClick,
            colors = ButtonDefaults.textButtonColors(
                contentColor = Color(0xFF06A0B5)
            ),
        ) {
            Text(
                text = "Sign Up",
                fontSize = 20.sp,
                color = Color(0xFF06A0B5)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
}