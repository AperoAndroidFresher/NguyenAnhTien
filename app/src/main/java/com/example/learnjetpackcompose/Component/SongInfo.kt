package com.example.learnjetpackcompose.Component

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SongInfo(title: String, artist: String) {
    Column(
        modifier = Modifier.padding(10.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = title,
            modifier = Modifier
                .padding(top = 6.dp)
                .width(170.dp)
                .basicMarquee(),
            style = MaterialTheme.typography.titleSmall,
            fontSize = 14.sp,
            color = Color.White
        )
        Text(
            text = artist,
            modifier = Modifier
                .width(170.dp)
                .basicMarquee(),
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 12.sp
        )
    }
}