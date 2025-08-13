package com.example.learnjetpackcompose.Component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnjetpackcompose.R

@Composable
fun ContentLoadFailure(
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.img_loading_failure),
            contentDescription = "Loading Failure",
            tint = Color.White,
            modifier = Modifier.height(68.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No internet connection,\nplease check your \nconnection again",
            color = Color.White,
            style = MaterialTheme.typography.titleSmall,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        MyButton(onClick = onClick, label = "Try Again")
    }
}