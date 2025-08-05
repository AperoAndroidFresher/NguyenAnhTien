package com.example.learnjetpackcompose.Screen.Playlist.Song

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.learnjetpackcompose.R
import com.example.learnjetpackcompose.RoomDB.Entity.Song
import com.example.learnjetpackcompose.Screen.Playlist.PlaylistViewModel


@Composable
fun SongCardList(song: Song,
                 onRemoveSong: (Song) -> Unit,
                 modifier: Modifier = Modifier){
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
            if (song.albumArt != null) {
                AsyncImage(
                    model = song.albumArt,
                    contentDescription = "Image Playlist",
                    modifier = Modifier
                        .size(64.dp)
                        .border(2.dp, Color.LightGray, CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(painter = painterResource(id = R.drawable.music_note),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp))
            }


            Column(){
                Text(
                    text = song.title,
                    modifier = Modifier.padding(10.dp).width(150.dp).basicMarquee(),
                    style = MaterialTheme.typography.titleSmall,
                    fontSize = 20.sp,
                    color = Color.White
                )
                Text(
                    text = song.artist,
                    modifier = Modifier.padding(start = 10.dp),
                    color = Color.White.copy(alpha = 0.7f),
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = song.duration.toString(),
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
                    shape = RoundedCornerShape(14.dp),
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
                                    painter = painterResource(R.drawable.icon_remove),
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
                                    painter = painterResource(R.drawable.icon_share),
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

                if (song.albumArt != null) {
                    AsyncImage(
                        model = song.albumArt,
                        contentDescription = "Image Playlist",
                        modifier = Modifier
                            .size(140.dp)
                            .border(2.dp, Color.LightGray, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(painter = painterResource(id = R.drawable.music_note),
                        contentDescription = null,
                        modifier = Modifier.size(140.dp))
                }

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
                        shape = RoundedCornerShape(14.dp),
                        expanded = showDropdownMenu,
                        onDismissRequest = { showDropdownMenu = false },
                        modifier = Modifier.clip(RoundedCornerShape(14.dp))
                            .background(Color.DarkGray.copy(alpha = 0.8f), RoundedCornerShape(14.dp))
                    ) {
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.icon_remove),
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
                                        painter = painterResource(R.drawable.icon_share),
                                        contentDescription = "Share",
                                        tint = Color.White,
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
                text = song.title,
                modifier = Modifier.padding(start = 10.dp).align(Alignment.CenterHorizontally).basicMarquee(),
                style = MaterialTheme.typography.titleSmall,
                fontSize = 20.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = song.artist,
                modifier = Modifier.padding(start = 10.dp).basicMarquee(),
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 18.sp

            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = song.duration,

                color = Color.White,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun SongLinear(
    modifier: Modifier,
    songs: List<Song>,
    onToggleView: () -> Unit,
    onRemoveSong: (Song) -> Unit,
    onReorder: (Int, Int) -> Unit
){

    val listState = rememberLazyListState()
//    val reorderState = rememberReorderableLazyListState(
//        listState = listState,
//        onMove = { from, to -> onReorder(from.index, to.index) }
//    )
    Column(modifier = modifier
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
                    modifier = Modifier.padding(10.dp).align(Alignment.CenterVertically)
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
                    modifier = Modifier.padding(10.dp)
                        .align(Alignment.CenterVertically)
                        .size(40.dp),
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

        LazyColumn(
            contentPadding = PaddingValues(8.dp),
        ){
            items(songs){playlist ->
                SongCardList(song = playlist, onRemoveSong = onRemoveSong )
            }
        }

//        LazyColumn(
//            state = listState,
//            modifier = Modifier
//                .fillMaxSize()
//        ) {
//            items(
//                items = songs,
//                key = { it.name }
//            ) { song ->
//                ReorderableItem(reorderState, key = song.name) { isDragging ->
//                    val elevation = if (isDragging) 8.dp else 4.dp
//                    SongCardList(
//                        song = song,
//                        onRemoveSong = onRemoveSong,
//                        modifier = Modifier
//                            .detectReorderAfterLongPress(reorderState)
//                            .graphicsLayer {
//                                shadowElevation = elevation.toPx()
//                            }
//                    )
//                }
//            }
//        }
    }
}

@Composable
fun SongGrid(
    modifier: Modifier,
    songs: List<Song>,
    onToggleView: () -> Unit, onRemoveSong: (Song) -> Unit){
    Column(
        modifier = modifier
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
                    modifier = Modifier.padding(10.dp).align(Alignment.CenterVertically)
                        .size(40.dp),
                    onClick = {
                        println("Return Playlist linear")
                        onToggleView()
                    },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.menu),
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
fun SongScreen(
    playlistId: Int,
    playlistTitle: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val playlistViewModel: PlaylistViewModel = hiltViewModel()
    val playlist = playlistViewModel.getPlaylistById(playlistId)
    val songs = playlist?.songs ?: emptyList()

    var isGridView by remember { mutableStateOf(false) }

    val removeSong: (Song) -> Unit = { songToRemove ->
        playlistViewModel.processIntent(
            com.example.learnjetpackcompose.Screen.Playlist.PlaylistIntent.RemoveSongFromPlaylist(
                playlistId, songToRemove
            )
        )
    }

    val reorder: (Int, Int) -> Unit = { from, to ->
        // Sẽ implement sau nếu cần
        println("Reorder from $from to $to")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Header với nút back và tên playlist
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = playlistTitle,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        // Hiển thị số lượng bài hát
        Text(
            text = "${songs.size} songs",
            color = Color.White.copy(alpha = 0.7f),
            modifier = Modifier.padding(horizontal = 16.dp),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (songs.isEmpty()) {
            // Hiển thị khi playlist trống
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(R.drawable.music_note),
                        contentDescription = "No songs",
                        tint = Color.Gray,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No songs in this playlist",
                        color = Color.Gray,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Add songs from the Library",
                        color = Color.Gray.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            // Hiển thị danh sách bài hát
            if (isGridView) {
                SongGrid(
                    modifier = Modifier.fillMaxSize(),
                    songs = songs,
                    onToggleView = { isGridView = false },
                    onRemoveSong = removeSong
                )
            } else {
                SongLinear(
                    modifier = Modifier.fillMaxSize(),
                    songs = songs,
                    onToggleView = { isGridView = true },
                    onRemoveSong = removeSong,
                    onReorder = reorder
                )
            }
        }
    }
}
