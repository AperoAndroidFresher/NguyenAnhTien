package com.example.learnjetpackcompose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import com.example.learnjetpackcompose.Screen.HomeScreen
import com.example.learnjetpackcompose.Screen.LogIn.LoginScreen
import com.example.learnjetpackcompose.Screen.LogIn.LoginViewModel

import com.example.learnjetpackcompose.Screen.SignUp.SignUpScreen
import com.example.learnjetpackcompose.Screen.SignUp.SignUpViewModel

@Composable
fun NavigationApp(){

    val backStack = rememberNavBackStack(LoginNavKey())
    val loginViewModel = remember { LoginViewModel() }
    val signUpViewModel = remember { SignUpViewModel() }
    NavDisplay(
        backStack = backStack,
        onBack = {backStack.removeLastOrNull()},
        entryProvider = entryProvider{
            entry<LoginNavKey>{key ->
                LoginScreen(
                    viewModel = loginViewModel,
                    onLoginClick = {
                        backStack.clear()
                        backStack.add(HomeNavKey) },
                    onSignUpClick = { backStack.add(SignUpNavKey())}
                )
            }

            entry<SignUpNavKey>{key ->
                SignUpScreen(
                    viewModel = signUpViewModel,
                    onBackClick = { backStack.removeLastOrNull() },
                    onSignUpClick = { backStack.add(LoginNavKey())}
                )
            }
            entry<HomeNavKey>{key ->
                HomeScreen(

                )
            }
        }
    )
}