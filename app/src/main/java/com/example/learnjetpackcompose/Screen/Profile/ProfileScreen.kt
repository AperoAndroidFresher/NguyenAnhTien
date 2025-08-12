package com.example.learnjetpackcompose.Screen.Profile

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.learnjetpackcompose.Component.IconButtonCustom
import com.example.learnjetpackcompose.Component.MyButton
import com.example.learnjetpackcompose.Screen.Profile.Component.HeaderProfile
import com.example.learnjetpackcompose.Screen.Profile.Component.PopupSuccess
import com.example.learnjetpackcompose.Screen.Profile.Component.ProfileInputField
import kotlinx.coroutines.flow.collectLatest
import java.io.File
import java.io.FileOutputStream


@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun MainProfileScreen(
    viewModel: ProfileViewModel,
    onLogout: () -> Unit,
    navigateToLogin: () -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var isDark by remember { mutableStateOf(true) }
    Surface(color = MaterialTheme.colorScheme.background) {
        if (isEditing) {
            LearnJetPackComposeTheme(darkTheme = isDark) {
                ProfileEditing(
                    viewModel = viewModel,
                    onBackToView = { isEditing = false },
                )
            }
        } else {
            LearnJetPackComposeTheme(darkTheme = isDark) {
                ProfileNoEdit(
                    viewModel = viewModel,
                    onEditClick = { isEditing = true },
                    isDark  = isDark, onToggleTheme = {isDark = !isDark},
                    logout = onLogout,
                    navigateToLogin = navigateToLogin
                )
            }
        }
    }
}

@Composable
fun ProfileNoEdit(
    viewModel: ProfileViewModel,
    onEditClick: () -> Unit,
    isDark: Boolean,
    onToggleTheme: () -> Unit,
    logout: () -> Unit = {},
    navigateToLogin: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is ProfileEffect.ShowError -> {
                    Log.e("ProfileScreen", "Error: ${effect.message}")
                }
                is ProfileEffect.NavigateToLogin -> {
                    navigateToLogin()
                }
                else -> {
                    // Handle other effects if needed
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.processIntent(ProfileIntent.LoadUserData)
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                IconButtonCustom(
                    onClick = { onToggleTheme() },
                    icon = if (isDark) R.drawable.nightmode else R.drawable.daymode,
                    title = "Switch Theme",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp)
                )
                Text(
                    text = "MY INFORMATION",
                    modifier = Modifier.align(Alignment.CenterVertically),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 24.sp
                )
                IconButtonCustom(onClick = { onEditClick() },
                    icon = R.drawable.icon_edit, title = "Edit",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            imageAvatar(state.imagePath.toString())

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ProfileInputField("NAME", state.displayName, {},
                    "Enter your name...", null,
                    isReadOnly =true, false, 1, Modifier.weight(1f))

                ProfileInputField("PHONE NUMBER", state.phoneNumber, {},
                    "Enter your phone...", null,
                    isReadOnly =true, false, 1, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            ProfileInputField("UNIVERSITY NAME", state.universityName, {},
                "Describe yourself...", null,
                isReadOnly =true, false, 1, Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(24.dp))
            ProfileInputField("DESCRIBE YOURSELF", state.description, {},
                "Describe yourself...", null,
                isReadOnly =true, false, 5, Modifier.fillMaxWidth())

            MyButton(onClick = logout,
                label = "Log out")
        }
    }
}


@Composable
fun ProfileEditing(
    viewModel: ProfileViewModel,
    onBackToView: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    var showSuccessPopup by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.processIntent(ProfileIntent.LoadUserData)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is ProfileEffect.NavigateBack -> {
                    showSuccessPopup = true
                    delay(2000)
                    showSuccessPopup = false
                    onBackToView()
                }
                is ProfileEffect.ProfileSaved -> {
                    showSuccessPopup = true
                }
                is ProfileEffect.ShowError -> {
                }

                ProfileEffect.NavigateToLogin -> {}
            }
        }
    }

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

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            HeaderProfile()
            Spacer(modifier = Modifier.height(24.dp))
            ImageAvatarEdit(state.imagePath.toString(), onclick = {imagePickerLauncher.launch("image/*")} )
            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ProfileInputField(
                    label = "NAME",
                    value = state.displayName,
                    onValueChange = { viewModel.processIntent(ProfileIntent.DisplayNameChanged(it)) },
                    placeholder = "Enter name...",
                    errorMessage = state.errors.displayNameError,
                    modifier = Modifier.weight(1f)
                )
                ProfileInputField(
                    label = "PHONE NUMBER",
                    value = state.phoneNumber,
                    onValueChange = { viewModel.processIntent(ProfileIntent.PhoneNumberChanged(it)) },
                    placeholder = "Your phone...",
                    errorMessage = state.errors.phoneNumberError,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            ProfileInputField(
                label = "UNIVERSITY NAME",
                value = state.universityName,
                onValueChange = { viewModel.processIntent(ProfileIntent.UniversityNameChanged(it)) },
                placeholder = "Your university name...",
                errorMessage = state.errors.universityNameError,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))
            ProfileInputField(
                label = "DESCRIBE YOURSELF",
                value = state.description,
                onValueChange = { viewModel.processIntent(ProfileIntent.DescriptionChanged(it)) },
                placeholder = "Enter a description...",
                errorMessage = null,
                singleLine = false,
                minLines = 5,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
            MyButton(onClick = {viewModel.processIntent(ProfileIntent.Submit)}, label = "Submit",
                containerColor = MaterialTheme.colorScheme.surfaceTint,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.height(50.dp)
            )

            if (showSuccessPopup) {
                PopupSuccess({showSuccessPopup = false})
                LaunchedEffect(Unit) {
                    delay(2000)
                    showSuccessPopup = false
                    onBackToView()
                }
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

@Composable
private fun imageAvatar(
    imagePath: String,

){
    Box(
        modifier = Modifier.fillMaxWidth()
    ){
        AsyncImage(
            model = imagePath,
            contentDescription = "Image Profile",
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .border(1.dp, Color.LightGray, CircleShape)
                .align(Alignment.Center),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.default_avatar),
            error = painterResource(R.drawable.default_avatar),
            fallback = painterResource(R.drawable.default_avatar)
        )
    }
}

@Composable
fun ImageAvatarEdit(
    imagePath: String,
    onclick: () -> Unit
){    Box(
    modifier = Modifier.fillMaxWidth()
){
    imageAvatar(imagePath)
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.5f)),
    ){
        IconButtonCustom(onclick, R.drawable.camera, "Edit",
            tint = Color.White, Modifier.size(30.dp))
    }
}

}


@Preview(showBackground = true, widthDp = 360)
@Composable
fun PreviewMainProfileScreen() {
    MaterialTheme {

    }
}
