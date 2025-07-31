package com.example.learnjetpackcompose.Screen.Profile

import android.annotation.SuppressLint
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
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.learnjetpackcompose.R
import com.example.learnjetpackcompose.ui.theme.LearnJetPackComposeTheme
import kotlinx.coroutines.delay
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.collectLatest
import java.io.File
import java.io.FileOutputStream


@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun MainProfileScreen() {
    var isEditing by remember { mutableStateOf(false) }
    var isDark by remember { mutableStateOf(false) }
    val viewModel = remember { ProfileViewModel() }
    Surface(color = MaterialTheme.colorScheme.background) {
        if (isEditing) {
            LearnJetPackComposeTheme(darkTheme = isDark) {
                ProfileEditing(
                    viewModel = viewModel,
                    onBackToView = { isEditing = false }
                )
            }
        } else {
            LearnJetPackComposeTheme(darkTheme = isDark) {
                ProfileNoEdit(
                    viewModel = viewModel,
                    onEditClick = { isEditing = true },
                    isDark  = isDark, onToggleTheme = {isDark = !isDark}
                )
            }
        }
    }
}

@Composable
fun ProfileNoEdit(
    viewModel: ProfileViewModel = ProfileViewModel(),
    onEditClick: () -> Unit,
    isDark: Boolean,
    onToggleTheme: () -> Unit
) {
    val state by viewModel.state.collectAsState()

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

        if (!state.imagePath.isNullOrEmpty()) {
            AsyncImage(
                model = state.imagePath,
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.LightGray, CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.rose),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.LightGray, CircleShape),
                contentScale = ContentScale.Crop
            )
        }

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
                    value = state.name,
                    readOnly = true,
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
                    value = state.phoneNumber,
                    readOnly = true,
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
                value = state.universityName,
                readOnly = true,
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
                value = state.description,
                readOnly = true,
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
fun ProfileEditing(
    viewModel: ProfileViewModel,
    onBackToView: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var showSuccessPopup by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is ProfileEffect.NavigateBack -> {
                    showSuccessPopup = true
                    delay(2000)
                    showSuccessPopup = false
                    onBackToView()
                }
            }
        }
    }

    // Launcher để chọn ảnh từ device
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val resizedImagePath = resizeAndSaveImage(context, it)
            resizedImagePath?.let { path ->
                viewModel.processIntent(ProfileIntent.ImagePathChanged(path))
            }
        }
    }

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

        Box(
            modifier = Modifier.fillMaxWidth()
        ){
            // Hiển thị ảnh dựa vào selectedImagePath hoặc ảnh mặc định
            if (state.imagePath != null) {
                AsyncImage(
                    model = state.imagePath,
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.LightGray, CircleShape)
                        .align(Alignment.Center),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.rose),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.LightGray, CircleShape)
                        .align(Alignment.Center),
                    contentScale = ContentScale.Crop
                )
            }

            Box(
                modifier = Modifier.align(Alignment.BottomCenter)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f)),
            ){
                IconButton(
                    onClick = {
                        // Mở image picker
                        imagePickerLauncher.launch("image/*")
                    }
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.camera),
                        contentDescription = "Edit",
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier
                            .size(30.dp)
                    )
                }
            }
        }

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
                    value = state.name,
                    onValueChange = {viewModel.processIntent(ProfileIntent.NameChanged(it))},
                    placeholder = { Text("Enter name...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary) },
                    colors = TextFieldDefaults.colors(MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = state.errors.nameError != null,
                    supportingText = {
                        state.errors.nameError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                    }
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(text = "PHONE NUMBER",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = state.phoneNumber,
                    onValueChange = {viewModel.processIntent(ProfileIntent.PhoneNumberChanged(it))},
                    placeholder = { Text("Your phone...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(MaterialTheme.colorScheme.primary),
                    singleLine = true,
                    isError = state.errors.phoneNumberError != null,
                    supportingText = {
                        state.errors.phoneNumberError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
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
                value = state.universityName,
                onValueChange = { viewModel.processIntent(ProfileIntent.UniversityNameChanged(it))},
                placeholder = { Text("Your university name...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary) },
                colors = TextFieldDefaults.colors(MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = state.errors.universityNameError != null,
                supportingText = {
                    state.errors.universityNameError?.let{Text(it, color = MaterialTheme.colorScheme.error)}
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
                value = state.description,
                onValueChange = { viewModel.processIntent(ProfileIntent.DescriptionChanged(it))},
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
                onClick = {viewModel.processIntent(ProfileIntent.Submit)},
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
                            .padding(24.dp),
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

fun resizeAndSaveImage(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()
        val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 300, 300, true)

        Log.d("A12", "${resizedBitmap?.byteCount} ${resizedBitmap?.width} ${resizedBitmap?.height}")
        val filename = "profile_image_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, filename)

        val outputStream = FileOutputStream(file)
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        outputStream.flush()
        outputStream.close()

        file.absolutePath
    }catch (e: Exception){
        e.printStackTrace()
        null
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun PreviewMainProfileScreen() {
    MaterialTheme {

    }
}
