package com.example.learnjetpackcompose.Screen.Login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit,
    onSignUpClick: () -> Unit
) {

    val state by viewModel.state.collectAsState()

    // Sử dụng LaunchedEffect để xử lý các sự kiện một lần
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest{ effect ->
            when (effect){
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
        modifier = Modifier.fillMaxSize().background(Color.Black)
    ){
        Image(
            painter = painterResource(id = R.drawable.aperologo),
            contentDescription = "Logo Apero",
            modifier = Modifier
                .size(350.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = "Login to your account",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

//      UserName Textfield
        OutlinedTextField(
            value = state.username,
            onValueChange = {viewModel.processIntent(LoginIntent.UsernameChanged(it))},
            label = { Text(text = "Username", color = Color.White.copy(0.7f)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User Icon",
                    tint = Color.White.copy(alpha = 0.5f)

                )
            }
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.password,
            onValueChange = {viewModel.processIntent(LoginIntent.PasswordChanged(it))},
            label = { Text(text = "Password", color = Color.White.copy(0.7f)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Password Icon",
                    tint = Color.White.copy(alpha = 0.5f)

                )
            },
            trailingIcon = {
                IconButton(onClick = {
                    viewModel.processIntent(LoginIntent.ShowPasswordVisibility)
                }
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.visible),
                        modifier = Modifier.size(24.dp),
                        contentDescription = "Show password",
                        tint = Color.White)
                }
            },
            visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),

        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ){
            Checkbox(
                checked = false,
                onCheckedChange = { viewModel.processIntent(LoginIntent.RememberMeChanged(it))},
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )

            Text(
                text = "Remember me",
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.align(Alignment.CenterVertically),
                fontSize = 20.sp
            )
        }

        Button(
            onClick = {viewModel.processIntent(LoginIntent.LoginClick)},
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp).height(50.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF06A0B5),
                contentColor = Color.White
            )
        ){
            Text(
                text = "Login",
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                text = "Don't have an account?",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            TextButton(
                onClick = onSignUpClick,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color(0xFF06A0B5)
                )
            ) {
                Text(
                    text = "Sign Up",
                    fontSize = 20.sp,
                    color = Color(0xFF06A0B5)
                )
            }
        }

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


@Preview (showBackground = true)
@Composable
fun MainScreenPreview() {

//    MainScreen()
}