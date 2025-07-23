package com.example.learnjetpackcompose.Lesson7_StateManagement
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.learnjetpackcompose.R

@Composable
fun DialogExample() {
    val showDialog = remember { mutableStateOf(false) }

    // Tự động đóng dialog sau 3 giây
    LaunchedEffect(showDialog.value) {
        if (showDialog.value) {
            delay(3000)
            showDialog.value = false
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { showDialog.value = true }) {
            Text("Hiển thị Dialog", fontSize = 16.sp)
        }

        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = { Text("Thông báo") },
                text = { Text("Đây là dialog sẽ tự đóng sau 3 giây") },
                confirmButton = {
                    Button(onClick = { showDialog.value = false }) {
                        Text("Đóng")
                    }
                }
            )
        }
    }
}


@Composable
fun MyCard(){
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ){
            Column(Modifier.padding(16.dp)){
                Text(text = "Title", fontSize = 24.sp)
                Text(text = "Description", fontSize = 16.sp)
                Text(text = "Phone number: 123456789", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun UserProfileCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.rose),
                contentDescription = "Ảnh đại diện",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "ROSE",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "BlackPink",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
//        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.rose),
                contentDescription = "Ảnh đại diện",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Rose",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Blackpink",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun UserProfileCard1() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth().padding(16.dp)
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.rose),
                    contentDescription = "Ảnh đại diện ROSE",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "ROSE",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "BlackPink",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Row {

                    OutlinedButton(
                        onClick = { },
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text("Profile")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { },
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text("Kết bạn")
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.rose),
                    contentDescription = "Ảnh đại diện ROSE",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "ROSE",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "BlackPink",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Row {

                    OutlinedButton(
                        onClick = { },
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Profile")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { },
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Blue,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Kết bạn")
                    }
                }
            }
        }
    }
}
@Preview (showBackground = true)
@Composable
fun DialogExamplePreview() {
//    DialogExample()
//    MyCard()
    UserProfileCard1()
}