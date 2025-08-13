package com.example.learnjetpackcompose.Screen.Player

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.learnjetpackcompose.R
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.example.learnjetpackcompose.Component.IconButtonCustom
import com.example.learnjetpackcompose.Screen.Player.Component.MiniSlider
import com.example.learnjetpackcompose.Screen.Player.Component.ProgressSlider
import kotlin.math.roundToLong


@Composable
fun PlayerScreen(
    onBackClick: () -> Unit = {},
    onExitClick: () -> Unit = {},
    onShuffleClick: () -> Unit = {},
    onPreviousClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    onRepeatClick: () -> Unit = {},
    viewModel: PlayerViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val isPlaying = viewModel.isPlaying.collectAsState().value
    val isShuffle = viewModel.isShuffle.collectAsState().value
    val repeatMode = viewModel.repeatMode.collectAsState().value
    val currentSong = viewModel.currentSong.collectAsState().value
    val currentPosition = viewModel.currentPosition.collectAsState().value
    val duration = viewModel.duration.collectAsState().value

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

    val handleSeek = { position: Long ->
        viewModel.seekTo(position)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Header(onBackClick, handleExitClick, modifier)
        Spacer(modifier = modifier.height(10.dp))
        Content(currentSong?.title, currentSong?.artist, currentSong?.albumArt)
        Spacer(modifier = modifier.height(20.dp))

        // Progress Slider Section
        ProgressSlider(
            currentPosition = currentPosition,
            duration = duration,
            onSeek = handleSeek,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = modifier.height(20.dp))
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButtonCustom(onBackClick,R.drawable.icon_back, "Back",
            tint = Color.White,Modifier.size(36.dp))

        Text(
            text = "Now Playing",
            style = MaterialTheme.typography.titleMedium,
            fontSize = 18.sp,
            color = Color.White
        )

        IconButtonCustom(onExitClick, R.drawable.icon_no, "Exit",
            tint = Color.White,Modifier.size(36.dp))
    }
}

@Composable
fun Content(
    songTitle: String? = "Unknown Title",
    songArtist: String? = "Unknown Artist",
    albumArtUrl: String? = null
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ){

        AsyncImage(
            model = albumArtUrl,
            contentDescription = "Album Art",
            modifier = Modifier
                .size(350.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.icon_music),
            error = painterResource(R.drawable.icon_music),
            fallback = painterResource(R.drawable.icon_music)
        )

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = songTitle.toString(),
            fontSize = 20.sp,
            color = Color.White,
        )
        Spacer(modifier = Modifier.height(8.dp))
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
        IconButtonCustom(
            onShuffleClick, R.drawable.icon_shuffle, "Shuffle", tint = Color.White,
            if (isShuffled) Modifier
                .size(36.dp)
                .background(Color(0xFF00C2CB).copy(0.5f))
            else Modifier.size(36.dp)
        )
        IconButtonCustom(
            onPreviousClick,
            R.drawable.icon_previous,
            "Previous",tint = Color.White,
            Modifier.size(36.dp)
        )
        IconButtonCustom(
            onPlayPauseClick,
            if (isPlaying) R.drawable.icon_pause else R.drawable.icon_play,
            if (isPlaying) "Pause" else "Play",
            tint = Color.White,
            Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFF00C2CB))
        )
        IconButtonCustom(onNextClick, R.drawable.icon_next, "Next",
            tint = Color.White, Modifier.size(36.dp))
        IconButtonCustom(
            onRepeatClick, R.drawable.icon_repeat, "Repeat",tint = Color.White,
            Modifier
                .size(36.dp)
                .background(Color(0xFF00C2CB).copy(0.5f))
        )
    }
}
