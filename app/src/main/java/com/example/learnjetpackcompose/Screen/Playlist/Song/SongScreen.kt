package com.example.learnjetpackcompose.Screen.Playlist.Song

import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.learnjetpackcompose.R
import com.example.learnjetpackcompose.RoomDB.Entity.Song
import com.example.learnjetpackcompose.Screen.Playlist.PlaylistViewModel
import androidx.compose.ui.platform.LocalContext
import com.example.learnjetpackcompose.Component.IconButtonCustom
import com.example.learnjetpackcompose.Screen.Playlist.PlaylistIntent
import com.example.learnjetpackcompose.Screen.Player.PlayerViewModel
import com.example.learnjetpackcompose.Utils.ShareUtils
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learnjetpackcompose.Screen.Playlist.Song.Component.SongCardGrid
import com.example.learnjetpackcompose.Screen.Playlist.Song.Component.SongCardList

@Composable
fun SongScreen(
    playlistId: Int,
    playlistTitle: String,
    onBackClick: () -> Unit,
    onSortClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val playlistViewModel: PlaylistViewModel = hiltViewModel()
    val playerViewModel: PlayerViewModel = hiltViewModel()
    val songViewModel: SongViewModel = viewModel()
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
        println("Reorder from $from to $to")
    }

    LaunchedEffect(Unit) {
        songViewModel.effect.collect { effect ->
            when (effect) {
                is SongEffect.ShowMessage -> Unit
                is SongEffect.ShareSongFile -> ShareUtils.shareAudioFile(appContext, effect.song)
            }
        }
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
                    onSortClick = onSortClick,
                    onRemoveSong = removeSong,
                    onPlaySong = { song ->
                        selectedSongId = song.songId
                        val startIndex = songs.indexOfFirst { it.songId == song.songId }.let { if (it >= 0) it else 0 }
                        val current = playerViewModel.currentSong.value
                        if (current?.songId == song.songId) {
                            playerViewModel.togglePlayPause()
                        } else {
                            playerViewModel.preparePlaylistPlayback(playlistId.toString(), songs, startIndex)
                            playerViewModel.playSong(song)
                        }
                    },
                    onSelectSong = { selectedSongId = it.songId },
                    onShareSong = { song -> songViewModel.processIntent(SongIntent.ShareSong(song)) }
                )
            } else {
                SongLinear(
                    playlistTitle = playlistTitle,
                    modifier = Modifier.fillMaxSize(),
                    songs = songs,
                    selectedSongId = selectedSongId,
                    onToggleView = { isGridView = true },
                    onRemoveSong = removeSong,
                    onSortClick = onSortClick,
                    onPlaySong = { song ->
                        selectedSongId = song.songId
                        val startIndex = songs.indexOfFirst { it.songId == song.songId }.let { if (it >= 0) it else 0 }
                        val current = playerViewModel.currentSong.value
                        if (current?.songId == song.songId) {
                            playerViewModel.togglePlayPause()
                        } else {
                            playerViewModel.preparePlaylistPlayback(playlistId.toString(), songs, startIndex)
                            playerViewModel.playSong(song)
                        }
                    },
                    onSelectSong = { selectedSongId = it.songId },
                    onShareSong = { song -> songViewModel.processIntent(SongIntent.ShareSong(song)) }
                )
            }
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
    onSortClick: () -> Unit,
    onPlaySong: (Song) -> Unit,
    onSelectSong: (Song) -> Unit,
    onShareSong: (Song) -> Unit,
    modifier: Modifier
){
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
                    onClick = onSortClick,
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
                    onSelectSong = onSelectSong,
                    onShareSong = onShareSong
                )
            }
        }
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
    onSortClick: () -> Unit,
    onSelectSong: (Song) -> Unit,
    onShareSong: (Song) -> Unit,
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
                    onClick = onSortClick,
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
                    onSelectSong = onSelectSong,
                    onShareSong = onShareSong
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

