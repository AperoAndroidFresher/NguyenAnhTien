package com.example.learnjetpackcompose.Screen.Playlist.Song

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.learnjetpackcompose.R
import com.example.learnjetpackcompose.RoomDB.Entity.Song
import com.example.learnjetpackcompose.Screen.Playlist.PlaylistViewModel
import androidx.compose.ui.platform.LocalContext
import com.example.learnjetpackcompose.Component.AlbumArt
import com.example.learnjetpackcompose.Component.DropdownMenuItemWithIcon
import com.example.learnjetpackcompose.Component.IconButtonCustom
import com.example.learnjetpackcompose.Component.SongInfo
import com.example.learnjetpackcompose.Screen.Playlist.PlaylistIntent
import com.example.learnjetpackcompose.Screen.Player.PlayerViewModel


@Composable
fun SongCardList(
    song: Song,
    isSelected: Boolean,
    onRemoveSong: (Song) -> Unit,
    onPlaySong: (Song) -> Unit,
    onSelectSong: (Song) -> Unit,
    modifier: Modifier = Modifier
){
    var showDropdownMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color.DarkGray else Color.Black
        )
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clickable {
                    onSelectSong(song)
                    onPlaySong(song)
                },
            verticalAlignment = Alignment.CenterVertically
        ){
            AlbumArt(song.albumArt)

            SongInfo(song.title, song.artist)

            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = song.duration,
                modifier = Modifier.align(Alignment.CenterVertically),
                color = Color.White,
                fontSize = 14.sp
            )

            Box(
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                IconButtonCustom(
                    onClick = {showDropdownMenu = true},
                    icon = R.drawable.icon_morevert,
                    title = "More options",
                )
                DropdownMenu(
                    shape = RoundedCornerShape(14.dp),
                    expanded = showDropdownMenu,
                    onDismissRequest = { showDropdownMenu = false },
                    modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(Color.DarkGray)
                ) {
                    DropdownMenuItemWithIcon(
                        iconId = R.drawable.icon_remove,
                        contentDescription = "Remove",
                        text = "Remove from playlist",
                        onClick = { onRemoveSong(song)
                            showDropdownMenu = false }
                    )
                    DropdownMenuItemWithIcon(
                        iconId = R.drawable.icon_share,
                        contentDescription = "Remove",
                        text = "Share",
                        onClick = { showDropdownMenu = false }
                    )
                }
            }
        }
    }
}

@Composable
fun SongCardGrid(
    song: Song,
    isSelected: Boolean,
    onRemoveSong: (Song) -> Unit,
    onPlaySong: (Song) -> Unit,
    onSelectSong: (Song) -> Unit
){
    var showDropdownMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color.DarkGray else Color.Black
        )
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onSelectSong(song)
                    onPlaySong(song)
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Box(){
                AlbumArt(song.albumArt, Modifier.size(140.dp))

                Box(
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {

                    IconButtonCustom(
                        {showDropdownMenu = true}, R.drawable.icon_morevert,
                        title = "More options",
                    )

                    DropdownMenu(
                        shape = RoundedCornerShape(14.dp),
                        expanded = showDropdownMenu,
                        onDismissRequest = { showDropdownMenu = false },
                        modifier = Modifier.clip(RoundedCornerShape(14.dp))
                            .background(Color.DarkGray.copy(alpha = 0.8f), RoundedCornerShape(14.dp))
                    ) {

                        DropdownMenuItemWithIcon(
                            iconId = R.drawable.icon_remove,
                            contentDescription = "Remove",
                            text = "Remove from playlist",
                            onClick = { onRemoveSong(song)
                                showDropdownMenu = false }
                        )
                        DropdownMenuItemWithIcon(
                            iconId = R.drawable.icon_share,
                            contentDescription = "Remove",
                            text = "Share",
                            onClick = { showDropdownMenu = false }
                        )
                    }
                }
            }
            SongGridInfo(song.title, song.artist, song.duration)
        }
    }
}

@Composable
fun SongLinear(
    playlistTitle: String,
    songs: List<Song>,
    selectedSongId: Long?,
    onToggleView: () -> Unit,
    onRemoveSong: (Song) -> Unit,
    onReorder: (Int, Int) -> Unit,
    onPlaySong: (Song) -> Unit,
    onSelectSong: (Song) -> Unit,
    modifier: Modifier
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
                text=playlistTitle,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(10.dp),
                color = Color.White
            )

            Row(){
                IconButtonCustom(
                    onClick = {onToggleView()},
                    icon = R.drawable.menu,
                    title = "Menu",
                    modifier = Modifier.padding(10.dp).align(Alignment.CenterVertically)
                )
                IconButtonCustom(
                    onClick = {},
                    icon = R.drawable.right_alignment,
                    title = "Sort",
                    modifier = Modifier.padding(10.dp).align(Alignment.CenterVertically)
                )
            }

        }

        LazyColumn(
            contentPadding = PaddingValues(8.dp),
        ){
            items(songs){playlist ->
                SongCardList(
                    song = playlist,
                    isSelected = selectedSongId == playlist.songId,
                    onRemoveSong = onRemoveSong,
                    onPlaySong = onPlaySong,
                    onSelectSong = onSelectSong
                )
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
    playlistTitle: String,
    songs: List<Song>,
    selectedSongId: Long?,
    onToggleView: () -> Unit,
    onRemoveSong: (Song) -> Unit,
    onPlaySong: (Song) -> Unit,
    onSelectSong: (Song) -> Unit,
    modifier: Modifier
){
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
                text= playlistTitle,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(10.dp),
                color = Color.White
            )

            Row(){
                IconButtonCustom(
                    onClick = {onToggleView()},
                    icon = R.drawable.menu,
                    title = "Menu",
                    modifier = Modifier.padding(10.dp).align(Alignment.CenterVertically)
                )
                IconButtonCustom(
                    onClick = {},
                    icon = R.drawable.right_alignment,
                    title = "Sort",
                    modifier = Modifier.padding(10.dp).align(Alignment.CenterVertically)
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2)
        ){
            items(songs){song ->
                SongCardGrid(
                    song = song,
                    isSelected = selectedSongId == song.songId,
                    onRemoveSong = onRemoveSong,
                    onPlaySong = onPlaySong,
                    onSelectSong = onSelectSong
                )
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
    val playerViewModel: PlayerViewModel = hiltViewModel()
    val playlistState by playlistViewModel.state.collectAsState()
    val songs = playlistState.playlists.find { it.playlistId == playlistId }?.songs ?: emptyList()
    val appContext = LocalContext.current.applicationContext

    var isGridView by remember { mutableStateOf(false) }
    var selectedSongId by remember { mutableStateOf<Long?>(null) }

    val removeSong: (Song) -> Unit = { songToRemove ->
        playlistViewModel.processIntent(
            PlaylistIntent.RemoveSongFromPlaylist(
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

        Spacer(modifier = Modifier.height(16.dp))

        if (songs.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                NoContent()
            }
        } else {
            if (isGridView) {
                SongGrid(
                    playlistTitle = playlistTitle,
                    modifier = Modifier.fillMaxSize(),
                    songs = songs,
                    selectedSongId = selectedSongId,
                    onToggleView = { isGridView = false },
                    onRemoveSong = removeSong,
                    onPlaySong = { song ->
                        selectedSongId = song.songId
                        // Set queue for Playlist context then start playback via ViewModel
                        val startIndex = songs.indexOfFirst { it.songId == song.songId }.let { if (it >= 0) it else 0 }
                        playerViewModel.setQueueFromPlaylist(playlistId.toString(), songs, startIndex)
                        playerViewModel.playSong(song)
                    },
                    onSelectSong = { selectedSongId = it.songId }
                )
            } else {
                SongLinear(
                    playlistTitle = playlistTitle,
                    modifier = Modifier.fillMaxSize(),
                    songs = songs,
                    selectedSongId = selectedSongId,
                    onToggleView = { isGridView = true },
                    onRemoveSong = removeSong,
                    onReorder = reorder,
                    onPlaySong = { song ->
                        selectedSongId = song.songId
                        // Set queue for Playlist context then start playback via ViewModel
                        val startIndex = songs.indexOfFirst { it.songId == song.songId }.let { if (it >= 0) it else 0 }
                        playerViewModel.setQueueFromPlaylist(playlistId.toString(), songs, startIndex)
                        playerViewModel.playSong(song)
                    },
                    onSelectSong = { selectedSongId = it.songId }
                )
            }
        }
    }
}

@Composable
fun NoContent(){
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
@Composable
private fun SongGridInfo(
    title: String,
    artist: String,
    duration: String
){
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = title,
            modifier = Modifier.padding(start = 10.dp).align(Alignment.CenterHorizontally).basicMarquee(),
            style = MaterialTheme.typography.titleSmall,
            fontSize = 18.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = artist,
            modifier = Modifier.padding(start = 10.dp).basicMarquee(),
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 14.sp

        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = duration,
            color = Color.White,
            fontSize = 16.sp
        )
    }
}
