package com.example.learnjetpackcompose.Screen.Profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
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
import androidx.compose.ui.window.Popup // Import Popup
import androidx.compose.ui.window.PopupProperties // Import PopupProperties (tùy chọn)
import com.example.learnjetpackcompose.R
import com.example.learnjetpackcompose.ui.theme.LearnJetPackComposeTheme
import kotlinx.coroutines.delay


@Composable
fun MainProfileScreen() {
    var isEditing by remember { mutableStateOf(false) }
    var isDark by remember { mutableStateOf(false) }
    Surface(color = MaterialTheme.colorScheme.background) {
        if (isEditing) {
            LearnJetPackComposeTheme(darkTheme = isDark) {
                ProfileEditing(
                    onBackToView = { isEditing = false }
                )
            }
        } else {
            LearnJetPackComposeTheme(darkTheme = isDark) {
                ProfileNoEdit(
                    onEditClick = { isEditing = true },
                    isDark  = isDark, onToggleTheme = {isDark = !isDark}
                )
            }
        }
    }
}

@Composable
fun ProfileNoEdit(modifier: Modifier = Modifier,
                  onEditClick: () -> Unit,
                  isDark: Boolean,
                  onToggleTheme: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .background(MaterialTheme.colorScheme.background)
                    .size(40.dp),
                onClick = {
                    onToggleTheme()
                },
            ) {
                Icon(
                    painter = painterResource( id = if (isDark) R.drawable.nightmode else R.drawable.daymode),
                    contentDescription = "Menu",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = "MY INFORMATION",
                modifier = Modifier.align(Alignment.CenterVertically),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 24.sp
            )
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterVertically).background(MaterialTheme.colorScheme.background)
                    .size(40.dp),
                onClick = {onEditClick()}
            ){
                Icon(
                    painter = painterResource(id = R.drawable.edit),
                    contentDescription = "Edit",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
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
                Text(text = "NAME",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = "",
                    shape = RoundedCornerShape(13.dp),
                    modifier = Modifier.border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(13.dp)),
                    onValueChange = {},
                    placeholder = { Text("Enter your name...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary)},
                    singleLine = true
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(text = "PHONE NUMBER",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = "",
                    shape = RoundedCornerShape(13.dp),
                    modifier = Modifier.border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(13.dp)),
                    onValueChange = {},
                    placeholder = { Text("Enter your phone...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary)},
                    singleLine = true
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "UNIVERSITY NAME",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = "",
                shape = RoundedCornerShape(13.dp),
                modifier = Modifier.fillMaxWidth().border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(13.dp)),
                onValueChange = {},
                placeholder = { Text("Enter university...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary)},
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "DESCRIBE YOURSELF",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = "",
                shape = RoundedCornerShape(13.dp),
                modifier = Modifier.fillMaxWidth().height(150.dp).border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(13.dp)),
                onValueChange = {},
                placeholder = { Text("Enter university...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary)},

                singleLine = false
            )
        }
    }
}


@Composable
fun ProfileEditing(onBackToView: () -> Unit) {

    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var universityName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var showSuccessPopup by remember { mutableStateOf(false) } // Đổi tên biến trạng thái thành Popup

    var nameError by remember { mutableStateOf(false) }
    var phoneNumberError by remember { mutableStateOf(false) }
    var universityNameError by remember { mutableStateOf(false) }

    val nameRegex = remember { "^[\\p{L} ]{1,30}$".toRegex() }
    val phoneNumberRegex = remember { "^[0-9]+$".toRegex() }
    val universityNameRegex = remember { "^[\\p{L} .'-]+$".toRegex() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
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
                modifier = Modifier.align(Alignment.CenterVertically),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 24.sp
            )
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
                Text(text = "NAME",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = name,

                    onValueChange = { newValue ->
                        name = newValue
                        nameError = !nameRegex.matches(newValue) && newValue.isNotEmpty()
                    },
                    placeholder = { Text("Enter name...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary) },
                    colors = TextFieldDefaults.colors(MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = nameError,
                    supportingText = {
                        if (nameError) {
                            Text("Chỉ chữ (A-Z, a-z, có dấu) và tối đa 30 ký tự",
                                color = MaterialTheme.colorScheme.error)
                        }
                    }
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(text = "PHONE NUMBER",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { newValue ->
                        phoneNumber = newValue
                        phoneNumberError = !phoneNumberRegex.matches(newValue) && newValue.isNotEmpty()
                    },
                    placeholder = { Text("Your phone...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary) },
//                    shape = RoundedCornerShape(13.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(MaterialTheme.colorScheme.primary),
                    singleLine = true,
                    isError = phoneNumberError,
                    supportingText = {
                        if (phoneNumberError) {
                            Text("Chỉ được nhập số",
                                color = MaterialTheme.colorScheme.error)
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "UNIVERSITY NAME",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = universityName,
                onValueChange = { newValue ->
                    universityName = newValue
                    universityNameError = !universityNameRegex.matches(newValue) && newValue.isNotEmpty()
                },
                placeholder = { Text("Your university name...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary) },
                colors = TextFieldDefaults.colors(MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = universityNameError,
                supportingText = {
                    if (universityNameError) {
                        Text("Chỉ được nhập chữ, dấu chấm, gạch ngang, nháy đơn",
                            color = MaterialTheme.colorScheme.error)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "DESCRIBE YOURSELF",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("Enter a description...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary) },
                shape = RoundedCornerShape(13.dp),
                modifier = Modifier.fillMaxWidth().height(150.dp).border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(13.dp)),

                singleLine = false
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        Column {
            Button(
                onClick = {
                    val isNameValid = nameRegex.matches(name)
                    val isPhoneNumberValid = phoneNumberRegex.matches(phoneNumber)
                    val isUniversityNameValid = universityNameRegex.matches(universityName)

                    nameError = !isNameValid
                    phoneNumberError = !isPhoneNumberValid
                    universityNameError = !isUniversityNameValid

                    if (isNameValid && isPhoneNumberValid && isUniversityNameValid) {
                        showSuccessPopup = true
                    }
                },
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceTint,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Submit",
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }


        if (showSuccessPopup) {
            Popup(
                onDismissRequest = { showSuccessPopup = false },
                properties = PopupProperties(focusable = true)
            ) {

                Box(
                    modifier = Modifier.size(300.dp, 200.dp)
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ){
                    Column(
                        modifier = Modifier
                            .wrapContentSize()
                            .background(Color.White, shape = RoundedCornerShape(8.dp))
                            .padding(24.dp), // Căn giữa nội dung trong Popup
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.success),
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


            LaunchedEffect(Unit) {
                delay(4000)
                showSuccessPopup = false
                onBackToView()
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun PreviewMainProfileScreen() {
    MaterialTheme {
        MainProfileScreen()
    }
}
