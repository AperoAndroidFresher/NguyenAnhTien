package com.example.learnjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier

import com.example.learnjetpackcompose.ui.theme.LearnJetPackComposeTheme

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.learnjetpackcompose.Screen.Playlist.PreviewChoosePlaylistDialog
import com.example.learnjetpackcompose.model.SongViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO)
//            != PackageManager.PERMISSION_GRANTED
//            && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//            != PackageManager.PERMISSION_GRANTED
//        ) {
//            // Xin quyền theo phiên bản Android
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(Manifest.permission.READ_MEDIA_AUDIO),
//                    100
//                )
//            } else {
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//                    100
//                )
//            }
//        } else {
//            // Đã có quyền => xử lý logic tiếp (ví dụ load danh sách nhạc)
////            val viewModel = SongViewModel(application)
//            setContent {
//                LearnJetPackComposeTheme {
//                    Surface(modifier = Modifier.fillMaxSize()) {
////                        PlaylistScreen(viewModel.songs)
//                        NavigationApp()
//                    }
//                }
//            }
//        }

        setContent {
            LearnJetPackComposeTheme {


                Surface(){

//                    NavigationApp()
                    PreviewChoosePlaylistDialog()

                }
            }
        }
    }
}










