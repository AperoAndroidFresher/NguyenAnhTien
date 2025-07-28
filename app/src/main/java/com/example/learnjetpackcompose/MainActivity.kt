package com.example.learnjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnjetpackcompose.Screen.*
import com.example.learnjetpackcompose.ui.theme.LearnJetPackComposeTheme
import kotlinx.coroutines.delay
import androidx.compose.foundation.Image as Image1

import com.example.learnjetpackcompose.SwitchTheme.PreviewSwitchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LearnJetPackComposeTheme {

//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//
////                    MyButton(modifier = Modifier.padding(innerPadding))
//
//                }
                Surface(){
//                    CustomEditing()
//                    MainProfileScreen()
//                    PlaylistPreview()
//                    PreviewSwitchTheme()
//                    SplashToLoginPreview()
//                    SignUpScreenPreview()
//                    MainScreenPreview()
                    NavigationApp()
                }
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
@Composable
fun MyButton(modifier: Modifier = Modifier){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Surface(
            color = Color.Blue,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Surface Text",
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 100.dp, vertical = 8.dp),
                color = (Color.White)
            )
        }
        Surface(
            color = Color.Red,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Compose 2",
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 100.dp, vertical = 8.dp),
                color = (Color.White)
            )
        }
        Surface(
            color = Color.Cyan,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Composeable 3",
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 100.dp, vertical = 8.dp),
                color = (Color.Black)
            )
        }
    }

}


@Composable
fun UiScreen(){
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        Box(
//            modifier = Modifier
//                .clip(CircleShape)
//                .border(2.dp, Color.Black, CircleShape)

        ){
            Image1(
                painter = painterResource(id = R.drawable.rose),
                contentDescription = "Image",
                modifier = Modifier.size(90.dp)
                    .clip(CircleShape)
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier.size(25.dp)
                    .background(Color.Green, CircleShape)
                    .align(Alignment.BottomEnd)
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),


        ){
            Text(text = "Nguyen Thuc Thuy Tien",
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                color = Color.Black,
                fontWeight = FontWeight.Bold)

            Text(text = "Active",
                fontSize = 18.sp,
                color = Color.Black,
                fontWeight = FontWeight.Normal)
        }



    }
}

@Composable
fun CustomButton(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
    content: String = "",
    backgroundColor: Color = Color.Blue,
    contentColor: Color = Color.Black
){
    Surface(
        color = backgroundColor,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp)
    ){
        Text(
            text =content,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 100.dp, vertical = 8.dp),
            color = contentColor
        )
    }
}

@Composable()
fun Practice() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CustomButton(content = "Button 1", backgroundColor = Color.Cyan, contentColor = Color.Black)
        CustomButton(content = "Button 2", backgroundColor = Color.Red, contentColor = Color.White)
        CustomButton(content = "Button 3", backgroundColor = Color.Blue, contentColor = Color.White)
    }
}

@Composable
fun CustomEditProfile() {
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var universityName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "MY INFORMATION",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
            )
//            Image(
//                painter = painterResource(id = R.drawable.edit),
//                contentDescription = "Edit",
//                modifier = Modifier.size(28.dp)
//            )
            Box(){
                Image(
                    painter = painterResource(id = R.drawable.edit),
                    contentDescription = "Edit",
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Image(
            painter = painterResource(id = R.drawable.rose),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .border(2.dp, Color.LightGray, CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(32.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "NAME", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("Enter name...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(text = "PHONE NUMBER", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    placeholder = { Text("Your phone...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- University Field (Bổ sung) ---
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "UNIVERSITY NAME", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = universityName,
                onValueChange = { universityName = it },
                placeholder = { Text("Your university name...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(24.dp))


        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "DESCRIBE YOURSELF", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("Enter a description...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                singleLine = false
            )
        }
    }
}

@Composable
fun CustomEditing() {
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var universityName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "MY INFORMATION",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
//            Box(
//                modifier = Modifier
//            ){
//                Image(
//                    painter = painterResource(id = R.drawable.edit),
//                    contentDescription = "Edit",
//                    modifier = Modifier.size(28.dp)
//                )
//            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Image(
            painter = painterResource(id = R.drawable.rose),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .border(2.dp, Color.LightGray, CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(32.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "NAME", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("Enter name...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(text = "PHONE NUMBER", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    placeholder = { Text("Your phone...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "UNIVERSITY NAME", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = universityName,
                onValueChange = { universityName = it },
                placeholder = { Text("Your university name...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(24.dp))


        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "DESCRIBE YOURSELF", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("Enter a description...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                singleLine = false
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        Column(){
            Button(
                onClick = {  },
                modifier = Modifier.width(150.dp).height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "Submit",
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.labelLarge,
                    )

            }
        }
    }
}

@Composable
fun UiStateSample(modifier: Modifier = Modifier){
    var count by remember {mutableStateOf(0)}

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ){
        Text(text = "$count", fontSize = 32.sp)
        Button(onClick = {count++}){
            Text(text = "Count Up", fontSize = 24.sp)
        }
    }
}


@Composable
fun NoEdit(modifier: Modifier = Modifier) {
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var university by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "MY INFORMATION",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )

            Image(
                painter = painterResource(id = R.drawable.edit),
                contentDescription = "Edit",
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(id = R.drawable.rose),
            contentDescription = "Rose",
            modifier = Modifier.size(200.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Gray, CircleShape)
        )
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(text = "Name", style = MaterialTheme.typography.labelLarge)

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Enter your name") },
                    singleLine = true
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth().weight(1f).padding(start = 8.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(text = "Phone", style = MaterialTheme.typography.labelLarge)

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Enter your Phone") },
                    singleLine = true
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "UNIVERSITY NAME", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = university,
                onValueChange = { university = it },
                placeholder = { Text("Your university name...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "DESCRIBE YOURSELF", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("Enter a description...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                singleLine = false
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { showDialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Submit")
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = null,
                text = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.success),
                            contentDescription = "Success Icon",
                            modifier = Modifier.size(80.dp).padding(8.dp)
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
                },
                confirmButton = {},
                dismissButton = {},
                modifier = Modifier
                    .wrapContentSize()
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            )

            LaunchedEffect(showDialog) {
                delay(2000)
                showDialog = false
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LearnJetPackComposeTheme {
//        Greeting("Android")
//        MyButton()
//        UiScreen()
//        Practice()
//        CustomEditProfile()
//        CustomEditing()
//        UiStateSample()
        NoEdit()
    }
}