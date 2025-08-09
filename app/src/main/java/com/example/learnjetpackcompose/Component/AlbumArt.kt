package com.example.learnjetpackcompose.Component

import android.net.Uri
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.learnjetpackcompose.R

@Composable
fun AlbumArt(albumArtUri: String?) {
    val uri = if (albumArtUri.isNullOrEmpty() || albumArtUri == Uri.EMPTY.toString()) {
        null
    } else {
        Uri.parse(albumArtUri)
    }

    AsyncImage(
        model = uri,
        contentDescription = "Album Art",
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Crop,
        placeholder = painterResource(R.drawable.icon_music),
        error = painterResource(R.drawable.icon_music),
        fallback = painterResource(R.drawable.icon_music)
    )
}