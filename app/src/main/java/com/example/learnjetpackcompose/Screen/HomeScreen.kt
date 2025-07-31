package com.example.learnjetpackcompose.Screen

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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnjetpackcompose.R
import com.example.learnjetpackcompose.model.NavBottomItems
import com.example.learnjetpackcompose.model.SongViewModel

//import com.example.learnjetpackcompose.model.songs

@Composable
fun HomeScreen(modifier: Modifier = Modifier, onMyProfileClick: () -> Unit){

    val navItemsList = listOf(
        NavBottomItems("Home", R.drawable.iconhome),
        NavBottomItems("Library", R.drawable.iconlibrary),
        NavBottomItems("My Playlist", R.drawable.iconplaylist)
    )
    var selectedIndex by remember { mutableStateOf(0) }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = { NavigationBar{
            navItemsList.forEachIndexed { index, navItem ->
                NavigationBarItem(
                    selected = selectedIndex == index,
                    onClick = { selectedIndex = index},
                    icon = {
                        Icon(
                            painter = painterResource(navItem.icon),
                            contentDescription = "Icon page",
                            modifier = Modifier.size(25.dp)
                        )
                    },
                    label = {Text(navItem.label, fontSize = 16.sp,
                        style = MaterialTheme.typography.labelMedium) }
                )
                }
            }
        }
    ) {innerPadding ->
        ContentScreen(modifier = Modifier.padding(innerPadding).fillMaxSize(), selectedIndex = selectedIndex, onMyProfileClick = onMyProfileClick)
    }
}

@Composable
fun HomePage(
    modifier: Modifier,
    onMyProfileClick:() -> Unit){
    Column(
        modifier = modifier,

        ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Spacer(modifier = Modifier.width(10.dp))
            IconButton(
                modifier = Modifier.padding(10.dp)
                    .size(40.dp).align(Alignment.CenterVertically),
                onClick = {
                    println("Go to playlist grid")
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
        Text(text = "Home Page", fontSize = 24.sp)
    }
}
@Composable
fun LibraryPage(
    modifier: Modifier
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Library Page", fontSize = 24.sp)
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex: Int, onMyProfileClick: () -> Unit) {
    val context = LocalContext.current
    val songViewModel = SongViewModel(context.applicationContext as Application)
    val songs = songViewModel.songs
    when(selectedIndex) {
        0 -> HomePage(
            modifier,
            onMyProfileClick = onMyProfileClick)
        1 -> LibraryPage(modifier)
        2 -> PlaylistScreen(modifier, songs)
    }
}