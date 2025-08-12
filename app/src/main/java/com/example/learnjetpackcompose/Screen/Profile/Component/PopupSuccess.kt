package com.example.learnjetpackcompose.Screen.Profile.Component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.learnjetpackcompose.R

@Composable
fun PopupSuccess(
    showSuccessPopup:() -> Unit,
){
    Popup(
        alignment = Alignment.Center,
        onDismissRequest = showSuccessPopup,
        properties = PopupProperties(focusable = true)
    ) {
        Box(
            modifier = Modifier
                .size(300.dp, 200.dp)
                .background(Color.White.copy(0.7f), shape = RoundedCornerShape(12.dp))
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ){
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_success),
                    contentDescription = "Success Icon",
                    modifier = Modifier
                        .size(80.dp)
                        .padding(8.dp)
                        .clip(CircleShape)
                        .background(Color.Green)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Success!",
                    fontWeight = FontWeight.Bold,
                    color = Color.Green,
                    modifier = Modifier
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Your information has been updated!",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}