package com.example.learnjetpackcompose.Screen.Home.Component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.learnjetpackcompose.Component.AlbumArt
import com.example.learnjetpackcompose.Screen.Home.HomeViewModel

@Composable
fun TopAlbumsScreen(
    onBack: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
){
    val state by viewModel.state.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize().background(Color.Black)
            .padding(12.dp)
    ){
        HeaderTopAlbums(onBack, "Top Albums")
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.allAlbums.album.size){ index ->
                val album = state.allAlbums.album[index]
                AlbumCard(
                    title = album.name,
                    artist = album.artist.name,
                    cover = album.image.lastOrNull()?.text
                )
            }
        }
    }
}

@Composable
fun AlbumCard(title: String, artist: String, cover: String?) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xD21F3A3A)),
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            AlbumArt(cover,
                modifier = Modifier.size(56.dp)
                    .clip(RoundedCornerShape(10.dp)))
            Column(modifier = Modifier.padding(start = 12.dp, end = 8.dp)) {
                Text(text = title, color = Color.White, fontSize = 14.sp, maxLines = 1)
                Text(text = artist, color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp, maxLines = 1)
            }
        }
    }
}