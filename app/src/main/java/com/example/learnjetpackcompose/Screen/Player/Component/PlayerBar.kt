package com.example.learnjetpackcompose.Screen.Player.Component

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnjetpackcompose.Component.IconButtonCustom
import com.example.learnjetpackcompose.R
import com.example.learnjetpackcompose.Screen.Player.PlayerViewModel
import formatTime
import kotlin.math.roundToLong

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
    val currentPosition = viewModel.currentPosition.collectAsState().value
    val totalDuration = viewModel.duration.collectAsState().value

    Box(
    ){
        Box(
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.TopEnd)
                .offset(x = 1.dp, y = (-24).dp)
                .background(Color.Transparent, shape = CircleShape)
        ){
            IconButtonCustom({viewModel.stopPlayback()},
                R.drawable.icon_no, "Stop", tint = Color.White,
                Modifier.size(36.dp))
        }
        PlayBarInfo(
            isPlaying = isPlaying,
            title = title,
            currentPosition = currentPosition,
            duration = totalDuration,
            onPlayPauseClick = onPlayPauseClick,
            onPlayerBarClick = onPlayerBarClick,
            onSeek = { position -> viewModel.seekTo(position) },
            modifier = modifier
        )
    }
}

@Composable
fun PlayBarInfo(
    isPlaying: Boolean,
    title: String,
    currentPosition: Long,
    duration: Long,
    onPlayPauseClick: () -> Unit,
    onPlayerBarClick: () -> Unit,
    onSeek: (Long) -> Unit,
    modifier: Modifier = Modifier
){
    var isUserSeeking by remember { mutableStateOf(false) }
    var seekPosition by remember { mutableStateOf(0f) }

    val progress = if (duration > 0) {
        if (isUserSeeking) seekPosition else (currentPosition.toFloat() / duration.toFloat())
    } else 0f

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF292929))
            .clickable { onPlayerBarClick() }
    ) {
        MiniSlider(
            value = progress,
            onValueChange = { newValue ->
                isUserSeeking = true
                seekPosition = newValue
            },
            onValueChangeFinished = {
                val newPosition = (seekPosition * duration).roundToLong()
                onSeek(newPosition)
                isUserSeeking = false
            },
            trackHeight = 3.dp,
            thumbRadius = 5.dp,
            activeColor = Color(0xFF00C2CB),
            inactiveColor = Color.White.copy(alpha = 0.3f),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            IconButtonCustom(onPlayPauseClick,
                if (isPlaying) R.drawable.icon_pause else R.drawable.icon_play,
                if (isPlaying) "Pause" else "Play",tint = Color.White,
                Modifier
                    .size(36.dp)
                    .clip(CircleShape))

            Text(
                text = title,
                fontSize = 16.sp,
                style = MaterialTheme.typography.titleSmall,
                color = Color.White,
                modifier = Modifier
                    .weight(2f)
                    .padding(horizontal = 10.dp)
                    .basicMarquee()
            )

            Text(
                text = "${formatTime(currentPosition)} / ${formatTime(duration)}",
                fontSize = 12.sp,
                color = Color.White.copy(0.7f),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}