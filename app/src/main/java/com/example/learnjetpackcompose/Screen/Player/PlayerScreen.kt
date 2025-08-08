package com.example.learnjetpackcompose.Screen.Player

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import coil.compose.AsyncImage
import com.example.learnjetpackcompose.R


@Preview
@Composable
fun PlayerScreen(
    onBackClick: () -> Unit ={},
    onExitClick: () -> Unit={},
    onShuffleClick: () -> Unit={},
    onPreviousClick: () -> Unit={},
    onPlayClick: () -> Unit={},
    onNextClick: () -> Unit={},
    onRepeatClick: () -> Unit={},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().background(Color.Black)
    ) {
        Header(onBackClick, onExitClick, modifier)
        Spacer(modifier = modifier.height(10.dp))
        Content()
        Spacer(modifier = modifier.height(10.dp))
        ButtonControls(onShuffleClick,
            onPreviousClick,
            onPlayClick,
            onNextClick,
            onRepeatClick)
    }
}

@Composable
private fun Header(
    onBackClick: () -> Unit,
    onExitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(
            onClick = onBackClick
        ) {
            Icon(
                painter = painterResource(R.drawable.icon_back),
                contentDescription = "Icon back",
                tint = Color.White
            )
        }

        Text(
            text = "Now Playing",
            style = MaterialTheme.typography.titleMedium,
            fontSize = 18.sp,
            color = Color.White
        )

        IconButton(
            onClick = onExitClick
        ) {
            Icon(
                painter = painterResource(R.drawable.icon_no),
                contentDescription = "Icon back",
                tint = Color.White
            )
        }

    }
}

@Composable
fun Content(){

    Column(
        modifier = Modifier.fillMaxWidth().padding(20.dp),
        verticalArrangement = Arrangement.Center
    ){
        Image(
            painter = painterResource(R.drawable.rose),
            contentDescription = "Default Playlist Image",
            modifier = Modifier.size(420.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Grainy days",
            fontSize = 18.sp,
            color = Color.White,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Artist",
            fontSize = 16.sp,
            color = Color.White.copy(0.7f),
        )
    }
}

@Composable
fun ButtonControls(
    onShuffleClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onPlayClick: () -> Unit,
    onNextClick: () -> Unit,
    onRepeatClick: () -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ){
        ButtonControl(onShuffleClick, "Shuffle", painterResource(R.drawable.icon_shuffle))
        ButtonControl(onPreviousClick, "Previous", painterResource(R.drawable.icon_previous))
        ButtonControl(onPlayClick, "Play", painterResource(R.drawable.icon_play))
        ButtonControl(onNextClick, "Next", painterResource(R.drawable.icon_next))
        ButtonControl(onRepeatClick, "Repeat", painterResource(R.drawable.icon_repeat))
    }
}

@Composable
fun ButtonControl(
    onClick: () -> Unit,
    title: String,
    painter: Painter
){
    IconButton(
        onClick = onClick
    ) {
        Icon(
            painter = painter,
            contentDescription = title,
            tint = Color.White
        )
    }
}

@Composable
fun PlayerBar(){
    Row(
        modifier = Modifier.fillMaxWidth().background(Color.DarkGray.copy(0.5f)).padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        IconButton(
            onClick = {},
        ) {
            Icon(
                painter = painterResource(R.drawable.icon_play),
                contentDescription = "Play",
                tint = Color.White
            )
        }

        Text(
            text = "Anh khong lam gi dau anh the",
            fontSize = 16.sp,
            style = MaterialTheme.typography.titleSmall,
            color = Color.White,
            modifier = Modifier.weight(2f).basicMarquee()
        )

        Text(
            text = "04:02",
            fontSize = 16.sp,
            color = Color.White.copy(0.7f)
        )
    }
}

