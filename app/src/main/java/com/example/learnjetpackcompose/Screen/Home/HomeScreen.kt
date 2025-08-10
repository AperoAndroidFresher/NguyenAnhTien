package com.example.learnjetpackcompose.Screen.Home

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.learnjetpackcompose.R
import com.example.learnjetpackcompose.RoomDB.Entity.SongViewModel
import com.example.learnjetpackcompose.Screen.Library.LibraryScreen
import com.example.learnjetpackcompose.Screen.Library.LibraryViewModel
import com.example.learnjetpackcompose.Screen.Playlist.PlaylistScreen
import com.example.learnjetpackcompose.Screen.Playlist.PlaylistViewModel
import com.example.learnjetpackcompose.data.model.NavBottomItems

@Composable
fun HomeScreen(
    onMyProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {

}

@Composable
fun HeaderHome(
    modifier: Modifier,
    onMyProfileClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.width(10.dp))
        IconButton(
            modifier = Modifier
                .padding(10.dp)
                .size(40.dp)
                .align(Alignment.CenterVertically),
            onClick = {
                println("Go to Profile Setting")
                onMyProfileClick()
            }
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Menu",
                tint = Color.Black
            )
        }
    }
}
