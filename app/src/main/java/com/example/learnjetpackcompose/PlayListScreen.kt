package com.example.learnjetpackcompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


data class Song(val name: String, val singer: String, val playtime: String, val imageId: Int)

@Composable
fun SongCardList(song: Song, onRemoveSong: (Song) -> Unit){
    var showDropdownMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth()
                .background(color = Color.Black),
        ){
            Image(
                painter = painterResource(id = song.imageId),
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(64.dp).clip(RoundedCornerShape(10.dp))

            )

            Column(){
                Text(
                    text = song.name,
                    modifier = Modifier.padding(10.dp).width(150.dp).basicMarquee(),
                    style = MaterialTheme.typography.titleSmall,
                    fontSize = 20.sp,
                    color = Color.White
                )
                Text(
                    text = song.singer,
                    modifier = Modifier.padding(start = 10.dp),
                    color = Color.White.copy(alpha = 0.7f),
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = song.playtime,
                modifier = Modifier.align(Alignment.CenterVertically),
                color = Color.White,
                fontSize = 20.sp
            )

            Box(
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                IconButton(
                    onClick = {
                        showDropdownMenu = true
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Mở menu tùy chọn",
                        tint = Color.White
                    )
                }

                DropdownMenu(
                    expanded = showDropdownMenu,
                    onDismissRequest = { showDropdownMenu = false },
                    modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(Color.DarkGray)
                ) {
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remove",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Remove from playlist",
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        },
                        onClick = {
                            onRemoveSong(song)
                            showDropdownMenu = false
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Share (coming soon)",
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        },
                        onClick = {
                            println("Share clicked - coming soon")
                            showDropdownMenu = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SongCardGrid(song: Song, onRemoveSong: (Song) -> Unit){
    var showDropdownMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ){
        Column(
            modifier = Modifier.fillMaxWidth()
                .background(color = Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Box(){

                Image(
                    painter = painterResource(id = song.imageId),
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(120.dp).clip(RoundedCornerShape(10.dp))
                )

                Box(
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    IconButton(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(30.dp)
                            .background(Color.DarkGray.copy(alpha = 0.6f)),
                        onClick = {
                            showDropdownMenu = true
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Mở menu tùy chọn",
                            tint = Color.White
                        )
                    }

                    DropdownMenu(
                        expanded = showDropdownMenu,
                        onDismissRequest = { showDropdownMenu = false },
                        modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(Color.DarkGray)
                    ) {
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Remove",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "Remove from playlist",
                                        color = Color.White,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            },
                            onClick = {
                                onRemoveSong(song)
                                showDropdownMenu = false
                            }
                        )

                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "Share",
                                        tint = Color.Gray,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "Share (coming soon)",
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            },
                            onClick = {
                                println("Share clicked - coming soon")
                                showDropdownMenu = false
                            }
                        )
                    }
                }
            }

            Text(
                text = song.name,
                modifier = Modifier.padding(6.dp),
                style = MaterialTheme.typography.titleSmall,
                fontSize = 20.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = song.singer,
//                modifier = Modifier.padding(10.dp),
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 18.sp

            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = song.playtime,

                color = Color.White,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun PlaylistLinear(songs: List<Song>, onToggleView: () -> Unit, onRemoveSong: (Song) -> Unit){
    Column(modifier = Modifier.fillMaxSize()
        .background(color = Color.Black)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Spacer(modifier = Modifier.width(26.dp))
            Text(
                text="MY PLAYLIST",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(10.dp),
                color = Color.White
            )

            Row(){
                IconButton(
                    modifier = Modifier.align(Alignment.CenterVertically)
                        .size(40.dp),
                    onClick = {
                        println("Go to playlist grid")
                        onToggleView()
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.menu),
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }

                IconButton(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onClick = {
                        println("More button clicked")
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.right_alignment),
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }
            }

        }

        LazyColumn(){
            items(songs){playlist ->
                SongCardList(song = playlist, onRemoveSong = onRemoveSong )
            }
        }
    }
}

@Composable
fun PlaylistGrid(songs: List<Song>, onToggleView: () -> Unit, onRemoveSong: (Song) -> Unit){
    Column(
        modifier = Modifier.fillMaxSize()
            .background(color = Color.Black)
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Spacer(modifier = Modifier.width(26.dp))
            Text(
                text="MY PLAYLIST",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(10.dp),
                color = Color.White
            )

            Row(){
                IconButton(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onClick = {
                        println("Return Playlist linear")
                        onToggleView()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }

                IconButton(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onClick = {
                        println("More button clicked")
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.right_alignment),
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2)
        ){
            items(songs){song ->
                SongCardGrid(song = song, onRemoveSong = onRemoveSong)
            }
        }
    }
}

@Composable
fun PlaylistScreen(listSongs: List<Song>) {
    var isGridView by remember { mutableStateOf(false) }
    var songs by remember { mutableStateOf(listSongs) }

    val removeSong: (Song) -> Unit = { songToRemove ->
        songs = songs.filter { it != songToRemove }
    }

    if (isGridView) {
        PlaylistGrid(
            songs = songs,
            onToggleView = { isGridView = false },
            onRemoveSong = removeSong
        )
    } else {
        PlaylistLinear(
            songs = songs,
            onToggleView = { isGridView = true },
            onRemoveSong = removeSong
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PlaylistPreview(){
    val songs = listOf(
        Song("Ordinary", "Alex Warren", "3:45", R.drawable.rose),
        Song("APT", "ROSE, Bruno Mars", "4:20", R.drawable.music1),
        Song("Azizam", "Ed Sheeran", "5:10", R.drawable.music2),
        Song("Bad Dreams", "Teddy Swims", "3:55", R.drawable.music3),
        Song("Anxiety", "Doechii", "4:40", R.drawable.music4),
        Song("Messy", "Rose", "5:25", R.drawable.music5),
        Song("BIRDS OF A FEATHER", "Billie Eilish", "3:30", R.drawable.music1),
        Song("back to friends", "sombr", "4:15", R.drawable.music2),
        Song("Sorry I'm Here for someone Else", "Benson Boone", "5:05", R.drawable.music3),
        Song("More to Lose", "Miley Cyrus", "3:50", R.drawable.music4),
        Song("Love Me Not", "Ravyn Lenae", "4:35", R.drawable.music5),
        Song("No One Noticed", "The Marias", "5:20", R.drawable.rose),
        Song("Die With A Smile", "Lady Gaga, Bruno Mars", "3:40", R.drawable.music2),
        Song("Luther", "Kendrick Lamar", "4:25", R.drawable.music3),
        Song("Lose Control", "Teddy Swims", "5:15", R.drawable.music4)
    )
//    PlaylistLinear(songs)
//    PlaylistGrid(songs)
    PlaylistScreen(songs)
}