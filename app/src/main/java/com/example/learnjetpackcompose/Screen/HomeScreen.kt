package com.example.learnjetpackcompose.Screen

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnjetpackcompose.R
import com.example.learnjetpackcompose.model.NavBottomItems
import com.example.learnjetpackcompose.model.songs

@Composable
fun HomeScreen(modifier: Modifier = Modifier, onMyProfileClick: () -> Unit){

    val navItemsList = listOf(
        NavBottomItems("Home"),
        NavBottomItems("Library"),
        NavBottomItems("My Playlist")
    )
    var selectedIndex by remember { mutableStateOf(0) }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = { NavigationBar{
            navItemsList.forEachIndexed { index, navItem ->
                NavigationBarItem(
                    selected = selectedIndex == index,
                    onClick = { selectedIndex = index},
                    icon = { /* Provide an icon for the item */ },
                    label = {Text(navItem.label, fontSize = 16.sp,
                        style = MaterialTheme.typography.labelMedium) }
                )
            }
        } }
    ) {innerPadding ->
        ContentScreen(modifier = Modifier.fillMaxSize().padding(innerPadding), selectedIndex = selectedIndex, onMyProfileClick = onMyProfileClick)
    }
}

@Composable
fun HomePage(onMyProfileClick:() -> Unit){
    Column(
        modifier = Modifier.fillMaxSize(),

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
fun LibraryPage(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Library Page", fontSize = 24.sp)
    }
}

@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex: Int, onMyProfileClick: () -> Unit) {
    when(selectedIndex) {
        0 -> HomePage(onMyProfileClick = onMyProfileClick)
        1 -> LibraryPage()
        2 -> PlaylistScreen(songs)
    }
}