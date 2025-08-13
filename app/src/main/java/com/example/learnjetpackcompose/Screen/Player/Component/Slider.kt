package com.example.learnjetpackcompose.Screen.Player.Component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import formatTime
import kotlin.math.roundToLong

@Composable
fun ProgressSlider(
    currentPosition: Long,
    duration: Long,
    onSeek: (Long) -> Unit,
    modifier: Modifier = Modifier,
    trackHeight: Dp = 3.dp,
    thumbRadius: Dp = 5.dp
) {
    var isUserSeeking by remember { mutableStateOf(false) }
    var seekPosition by remember { mutableStateOf(0f) }

    val progress = if (duration > 0) {
        if (isUserSeeking) seekPosition else (currentPosition.toFloat() / duration.toFloat())
    } else 0f

    Column(modifier = modifier.fillMaxWidth()) {
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
            trackHeight = trackHeight,
            thumbRadius = thumbRadius,
            activeColor = Color(0xFF00C2CB),
            inactiveColor = Color.White.copy(alpha = 0.3f),
            modifier = Modifier.fillMaxWidth()
        )

        // Time labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatTime(
                    if (isUserSeeking) (seekPosition * duration).roundToLong() else currentPosition
                ),
                color = Color.White.copy(0.7f),
                fontSize = 12.sp
            )
            Text(
                text = formatTime(duration),
                color = Color.White.copy(0.7f),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun MiniSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    trackHeight: Dp = 3.dp,
    thumbRadius: Dp = 5.dp,
    activeColor: Color = Color(0xFF00C2CB),
    inactiveColor: Color = Color.White.copy(alpha = 0.3f)
) {
    val thumbPx = with(LocalDensity.current) { thumbRadius.toPx() }
    val trackPx = with(LocalDensity.current) { trackHeight.toPx() }

    Box(
        modifier = modifier
            .height(thumbRadius * 2)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = { onValueChangeFinished?.invoke() }
                ) { change, _ ->
                    val newValue = (change.position.x / size.width).coerceIn(0f, 1f)
                    onValueChange(newValue)
                }
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Inactive track
            drawRoundRect(
                color = inactiveColor,
                cornerRadius = CornerRadius(trackPx / 2, trackPx / 2),
                size = Size(size.width, trackPx),
                topLeft = Offset(0f, (size.height - trackPx) / 2)
            )

            // Active track
            drawRoundRect(
                color = activeColor,
                cornerRadius = CornerRadius(trackPx / 2, trackPx / 2),
                size = Size(size.width * value, trackPx),
                topLeft = Offset(0f, (size.height - trackPx) / 2)
            )

            // Thumb
            drawCircle(
                color = activeColor,
                radius = thumbPx,
                center = Offset(size.width * value, size.height / 2)
            )
        }
    }
}