package com.example.learnjetpackcompose.Component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun IconButtonCustom(
    onClick:() -> Unit,
    icon: Int,
    title: String,
    tint: Color = Color.White,
    modifier: Modifier = Modifier
){
    IconButton(
        onClick = onClick,
        modifier = modifier.size(36.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = title,
            tint = tint,
            modifier = Modifier.size(24.dp)
        )
    }
}