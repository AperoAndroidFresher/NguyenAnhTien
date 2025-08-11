package com.example.learnjetpackcompose.Screen.Player

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.learnjetpackcompose.R
import com.example.learnjetpackcompose.data.model.PlaybackManager
import com.example.learnjetpackcompose.data.service.MusicService
import androidx.compose.runtime.collectAsState
import com.example.learnjetpackcompose.Component.IconButtonCustom


@Composable
fun PlayerScreen(
    onBackClick: () -> Unit = {},
    onExitClick: () -> Unit = {},
    onShuffleClick: () -> Unit = {},
    onPreviousClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    onRepeatClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel = viewModel()
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val isPlaying = viewModel.isPlaying.collectAsState().value
    val isShuffle = viewModel.isShuffle.collectAsState().value
    val repeatMode = viewModel.repeatMode.collectAsState().value
    val currentSong = viewModel.currentSong.collectAsState().value

    val handlePlayClick = {
        viewModel.togglePlayPause()
    }

    val handleExitClick = {
        viewModel.stopPlayback()
        onExitClick()
    }

    val handlePreviousClick = {
        viewModel.skipToPrevious()
        onPreviousClick()
    }

    val handleNextClick = {
        viewModel.skipToNext()
        onNextClick()
    }
    val handleShuffleClick = {
        viewModel.toggleShuffle()
        onShuffleClick()
    }
    val handleRepeatClick = {
        viewModel.cycleRepeatMode()
        onRepeatClick()
    }

    Column(
        modifier = modifier.fillMaxSize().background(Color.Black)
    ) {
        Header(onBackClick, handleExitClick, modifier)
        Spacer(modifier = modifier.height(10.dp))
        Content(currentSong?.title, currentSong?.artist, currentSong?.albumArt)
        Spacer(modifier = modifier.height(10.dp))
        ButtonControls(handleShuffleClick,
            handlePreviousClick,
            handlePlayClick,
            handleNextClick,
            handleRepeatClick,
            isPlaying,
            isShuffle,
            repeatMode,
            modifier)
    }
}

@Preview
@Composable
fun PlayerScreenPreview() {
    PlayerScreen()
}

@Composable
private fun Header(
    onBackClick: () -> Unit,
    onExitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButtonCustom(onBackClick,R.drawable.icon_back, "Back", Modifier.size(36.dp))

        Text(
            text = "Now Playing",
            style = MaterialTheme.typography.titleMedium,
            fontSize = 18.sp,
            color = Color.White
        )

        IconButtonCustom(onExitClick, R.drawable.icon_no, "Exit", Modifier.size(36.dp))
    }
}

@Composable
fun Content(
    songTitle: String? = "Unknown Title",
    songArtist: String? = "Unknown Artist",
    albumArtUrl: String? = null
){
    Column(
        modifier = Modifier.fillMaxWidth().padding(20.dp),
        verticalArrangement = Arrangement.Center
    ){

        AsyncImage(
            model = albumArtUrl,
            contentDescription = "Album Art",
            modifier = Modifier
                .size(400.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.icon_music),
            error = painterResource(R.drawable.icon_music),
            fallback = painterResource(R.drawable.icon_music)
        )

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = songTitle.toString(),
            fontSize = 18.sp,
            color = Color.White,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = songArtist.toString(),
            fontSize = 16.sp,
            color = Color.White.copy(0.7f),
        )
    }
}

@Composable
fun ButtonControls(
    onShuffleClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onNextClick: () -> Unit,
    onRepeatClick: () -> Unit,
    isPlaying: Boolean = false,
    isShuffled: Boolean = false,
    isRepeated: RepeatMode = RepeatMode.REPEAT_ONE,
    modifier: Modifier = Modifier
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButtonCustom(onShuffleClick, R.drawable.icon_shuffle,"Shuffle",
            if (isShuffled) Modifier.size(36.dp).background(Color.White.copy(0.5f)) else Modifier.size(36.dp))
        IconButtonCustom(onPreviousClick, R.drawable.icon_previous,"Previous", Modifier.size(36.dp))
        IconButtonCustom(onPlayPauseClick,
            if (isPlaying) R.drawable.icon_pause else R.drawable.icon_play,
            if (isPlaying) "Pause" else "Play",
            Modifier.size(36.dp))
        IconButtonCustom(onNextClick, R.drawable.icon_next,"Next", Modifier.size(36.dp))
        IconButtonCustom(onRepeatClick, R.drawable.icon_repeat,"Repeat", Modifier.size(36.dp))
    }
}

@Composable
fun PlayerBar(
    title: String = "",
    duration: String = "",
    isPlaying: Boolean = false,
    onPlayPauseClick: () -> Unit = {},
    onPlayerBarClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel
){
    Box(
    ){
        Box(
            modifier = Modifier.size(24.dp)
                .align(Alignment.TopEnd)
                .offset(x = 1.dp, y = (-24).dp)
                .background(Color.Transparent, shape = CircleShape)
        ){
            IconButtonCustom({viewModel.stopPlayback()},
                R.drawable.icon_no, "Stop",
                Modifier.size(36.dp))
        }
        PlayBarInfo(isPlaying, title, duration,
            onPlayPauseClick, onPlayerBarClick, modifier)
    }
}

@Composable
fun PlayBarInfo(
    isPlaying: Boolean,
    title: String,
    duration: String,
    onPlayPauseClick: () -> Unit,
    onPlayerBarClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF292929))
            .clickable { onPlayerBarClick() }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        IconButtonCustom(onPlayPauseClick,
            if (isPlaying) R.drawable.icon_pause else R.drawable.icon_play,
            if (isPlaying) "Pause" else "Play",
            Modifier.size(36.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            style = MaterialTheme.typography.titleSmall,
            color = Color.White,
            modifier = Modifier.weight(2f).padding(10.dp).basicMarquee()
        )

        Text(
            text = duration,
            fontSize = 16.sp,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
