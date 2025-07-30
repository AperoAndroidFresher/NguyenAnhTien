package com.example.learnjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface

import com.example.learnjetpackcompose.ui.theme.LearnJetPackComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LearnJetPackComposeTheme {

//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//
////                    MyButton(modifier = Modifier.padding(innerPadding))
//
//                }
                Surface(){
//                    CustomEditing()
//                    MainProfileScreen()
//                    PlaylistPreview()
//                    PreviewSwitchTheme()
//                    SplashToLoginPreview()
//                    SignUpScreenPreview()
//                    MainScreenPreview()
                    NavigationApp()
//                    ProfileTestPreview()
//                    Test2Preview()
//                    PreviewMainProfileScreen()
                }
            }
        }
    }
}








