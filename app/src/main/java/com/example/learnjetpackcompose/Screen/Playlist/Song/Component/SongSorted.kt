package com.example.learnjetpackcompose.Screen.Playlist.Song.Component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnjetpackcompose.Component.IconButtonCustom
import com.example.learnjetpackcompose.R
import com.example.learnjetpackcompose.RoomDB.Entity.Song

@Composable
fun SongSorted(
    songs: List<Song>,
    onBackClick:() -> Unit = {},
    onSaveClick:() -> Unit = {},
){
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.Black)
            .padding(12.dp)
    ){
        HeaderSorted(onBackClick, onSaveClick)

        
    }


}

@Preview
@Composable
fun HeaderSorted(
    onBackClick:() -> Unit = {},
    onSaveClick:() -> Unit = {},
    modifier: Modifier = Modifier
){
    Row(
        modifier = Modifier.fillMaxWidth().padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        IconButtonCustom(onBackClick, R.drawable.icon_back, "Back", Color.White)
        Text("Sorting", style = MaterialTheme.typography.headlineMedium, color = Color.White, fontSize = 20.sp )
        IconButtonCustom(onSaveClick, R.drawable.icon_yes, "Save", Color.White)
    }
}