package com.example.learnjetpackcompose

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp



data class SongTest(val id: String,
                val title: String,
                val artist: String,
                var isFavorite: Boolean = false)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongItem(song: SongTest, onFavoriteClick: (SongTest) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = song.title, style = MaterialTheme.typography.titleMedium)
                Text(text = song.artist, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            IconButton(onClick = { onFavoriteClick(song) }) {
                Icon(
                    imageVector = if (song.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (song.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


@Composable
fun SongList(songs: List<SongTest>, onSongFavoriteToggle: (SongTest) -> Unit) {
    Column {
        Column(
            modifier = Modifier.height(150.dp).padding(16.dp),
            verticalArrangement = Arrangement.Center
        ){
           Text(
            text = "Song List",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth())
        }

        LazyColumn(
            modifier = Modifier.height(500.dp)
        ) {
            items(songs) { song ->
                SongItem(song = song, onFavoriteClick = onSongFavoriteToggle)
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSongList() {
    val sampleSongs = remember { mutableStateListOf(
        SongTest(id = "1", title = "Shape of You", artist = "Ed Sheeran", isFavorite = false),
        SongTest(id = "2", title = "Blinding Lights", artist = "The Weeknd", isFavorite = true),
        SongTest(id = "3", title = "Uptown Funk1", artist = "Mark Ronson ft. Bruno Mars", isFavorite = false),
        SongTest(id = "4", title = "Uptown Funk2", artist = "Mark Ronson ft. Bruno Mars", isFavorite = false),
        SongTest(id = "5", title = "APT APT", artist = "ROSE ft. Bruno Mars", isFavorite = true),
        SongTest(id = "6", title = "Uptown Funk3", artist = "Mark Ronson ft. Bruno Mars", isFavorite = false),
        SongTest(id = "7", title = "Uptown Funk4", artist = "Mark Ronson ft. Bruno Mars", isFavorite = false),
        SongTest(id = "8", title = "My love", artist = "Mark Ronson ft. Bruno Mars", isFavorite = false),
        SongTest(id = "9", title = "Uptown Funk5", artist = "Mark Ronson ft. Bruno Mars", isFavorite = true),
        SongTest(id = "10", title = "Uptown Funk6", artist = "Mark Ronson ft. Bruno Mars", isFavorite = false),
        SongTest(id = "11", title = "Uptown Funk7", artist = "Mark Ronson ft. Bruno Mars", isFavorite = false),
        SongTest(id = "12", title = "Uptown Funk8", artist = "Mark Ronson ft. Bruno Mars", isFavorite = false),
        SongTest(id = "12", title = "Uptown Funk9", artist = "Mark Ronson ft. Bruno Mars", isFavorite = false)
    )}

    MaterialTheme {
        SongList(songs = sampleSongs, onSongFavoriteToggle = { song ->
            val index = sampleSongs.indexOfFirst { it.id == song.id }
            if (index != -1) {
                sampleSongs[index] = sampleSongs[index].copy(isFavorite = !song.isFavorite)
            }
        })
    }
}