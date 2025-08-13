package com.example.learnjetpackcompose.Screen.Home.Component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnjetpackcompose.Component.IconButtonCustom
import com.example.learnjetpackcompose.R

@Preview
@Composable
fun HeaderTopAlbums(
    onBack:() -> Unit = {},
    title: String = "Top Albums"
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButtonCustom(
            onBack, R.drawable.icon_back,
            title = "Back",
            tint = Color.White
        )
        Text(text = title, fontSize = 20.sp, color = Color.White)
    }
}