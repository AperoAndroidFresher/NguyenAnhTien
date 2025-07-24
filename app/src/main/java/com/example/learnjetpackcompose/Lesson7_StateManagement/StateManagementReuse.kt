package com.example.learnjetpackcompose.Lesson7_StateManagement

import com.example.learnjetpackcompose.R

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import kotlinx.coroutines.delay

// Data class để quản lý dữ liệu profile
data class ProfileData(
    val name: String = "",
    val phoneNumber: String = "",
    val universityName: String = "",
    val description: String = ""
)

// Data class để quản lý validation
data class ValidationState(
    val nameError: Boolean = false,
    val phoneNumberError: Boolean = false,
    val universityNameError: Boolean = false
)

// Validation rules
object ValidationRules {
    val nameRegex = "^[\\p{L} ]{1,30}$".toRegex()
    val phoneNumberRegex = "^[0-9]+$".toRegex()
    val universityNameRegex = "^[\\p{L} .'-]+$".toRegex()

    fun validateName(name: String): Boolean = nameRegex.matches(name)
    fun validatePhoneNumber(phone: String): Boolean = phoneNumberRegex.matches(phone)
    fun validateUniversityName(university: String): Boolean = universityNameRegex.matches(university)

    fun validateAll(profile: ProfileData): ValidationState {
        return ValidationState(
            nameError = !validateName(profile.name) && profile.name.isNotEmpty(),
            phoneNumberError = !validatePhoneNumber(profile.phoneNumber) && profile.phoneNumber.isNotEmpty(),
            universityNameError = !validateUniversityName(profile.universityName) && profile.universityName.isNotEmpty()
        )
    }

    fun isValidProfile(profile: ProfileData): Boolean {
        return validateName(profile.name) &&
                validatePhoneNumber(profile.phoneNumber) &&
                validateUniversityName(profile.universityName)
    }
}

@Composable
fun MainProfileScreen() {
    var isEditing by remember { mutableStateOf(false) }
    var profileData by remember { mutableStateOf(ProfileData()) }

    Surface(color = MaterialTheme.colorScheme.background) {
        if (isEditing) {
            ProfileEditingScreen(
                initialData = profileData,
                onBackToView = { updatedData ->
                    profileData = updatedData
                    isEditing = false
                }
            )
        } else {
            ProfileViewScreen(
                profileData = profileData,
                onEditClick = { isEditing = true }
            )
        }
    }
}

@Composable
fun ProfileViewScreen(
    profileData: ProfileData,
    onEditClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header với nút edit
        ProfileHeader(
            title = "MY INFORMATION",
            showEditButton = true,
            onEditClick = onEditClick
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Profile Image
        ProfileImage()

        Spacer(modifier = Modifier.height(32.dp))

        // Profile Fields
        ProfileFormFields(
            profileData = profileData,
            isEditing = false,
            onDataChange = { /* Không làm gì trong view mode */ },
            validationState = ValidationState()
        )
    }
}

@Composable
fun ProfileEditingScreen(
    initialData: ProfileData,
    onBackToView: (ProfileData) -> Unit
) {
    var profileData by remember { mutableStateOf(initialData) }
    var validationState by remember { mutableStateOf(ValidationState()) }
    var showSuccessPopup by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        ProfileHeader(
            title = "MY INFORMATION",
            showEditButton = false
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Profile Image
        ProfileImage()

        Spacer(modifier = Modifier.height(32.dp))

        // Editable Form Fields
        ProfileFormFields(
            profileData = profileData,
            isEditing = true,
            onDataChange = { newData ->
                profileData = newData
                validationState = ValidationRules.validateAll(newData)
            },
            validationState = validationState
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Submit Button
        SubmitButton(
            onClick = {
                val newValidationState = ValidationRules.validateAll(profileData)
                validationState = newValidationState

                if (ValidationRules.isValidProfile(profileData)) {
                    showSuccessPopup = true
                }
            }
        )

        // Success Popup
        if (showSuccessPopup) {
            SuccessPopup(
                onDismiss = { showSuccessPopup = false }
            )

            LaunchedEffect(Unit) {
                delay(4000)
                showSuccessPopup = false
                onBackToView(profileData)
            }
        }
    }
}

@Composable
fun ProfileHeader(
    title: String,
    showEditButton: Boolean = false,
    onEditClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (showEditButton) Arrangement.SpaceBetween else Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showEditButton) {
            Spacer(modifier = Modifier.width(16.dp))
        }

        Text(
            text = title,
            fontSize = if (showEditButton) 28.sp else 20.sp,
            fontWeight = FontWeight.Bold,
        )

        if (showEditButton) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clickable { onEditClick() },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.edit),
                    contentDescription = "Edit Profile",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun ProfileImage(
    imageRes: Int = R.drawable.rose,
    size: Int = 140
) {
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = "Profile Image",
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .border(2.dp, Color.LightGray, CircleShape),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun ProfileFormFields(
    profileData: ProfileData,
    isEditing: Boolean,
    onDataChange: (ProfileData) -> Unit,
    validationState: ValidationState
) {
    // Name và Phone Number row
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CustomTextField(
            label = "NAME",
            value = profileData.name,
            placeholder = if (isEditing) "Enter name..." else "Enter your name...",
            onValueChange = { newValue ->
                onDataChange(profileData.copy(name = newValue))
            },
            isEditing = isEditing,
            isError = validationState.nameError,
            errorMessage = "Chỉ chữ (A-Z, a-z, có dấu) và tối đa 30 ký tự",
            modifier = Modifier.weight(1f)
        )

        CustomTextField(
            label = "PHONE NUMBER",
            value = profileData.phoneNumber,
            placeholder = if (isEditing) "Your phone..." else "Enter your phone...",
            onValueChange = { newValue ->
                onDataChange(profileData.copy(phoneNumber = newValue))
            },
            isEditing = isEditing,
            isError = validationState.phoneNumberError,
            errorMessage = "Chỉ được nhập số",
            modifier = Modifier.weight(1f)
        )
    }

    Spacer(modifier = Modifier.height(24.dp))

    // University Name
    CustomTextField(
        label = "UNIVERSITY NAME",
        value = profileData.universityName,
        placeholder = if (isEditing) "Your university name..." else "Enter University...",
        onValueChange = { newValue ->
            onDataChange(profileData.copy(universityName = newValue))
        },
        isEditing = isEditing,
        isError = validationState.universityNameError,
        errorMessage = "Chỉ được nhập chữ, dấu chấm, gạch ngang, nháy đơn",
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(24.dp))

    // Description
    CustomTextField(
        label = "DESCRIBE YOURSELF",
        value = profileData.description,
        placeholder = if (isEditing) "Enter a description..." else "Enter Description...",
        onValueChange = { newValue ->
            onDataChange(profileData.copy(description = newValue))
        },
        isEditing = isEditing,
        isError = false,
        errorMessage = "",
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        singleLine = false
    )
}

@Composable
fun CustomTextField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    isEditing: Boolean,
    isError: Boolean,
    errorMessage: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true
) {
    Column(modifier = modifier) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                if (isEditing) {
                    onValueChange(newValue)
                }
            },
            placeholder = { Text(placeholder, style = MaterialTheme.typography.bodyMedium) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = singleLine,
            isError = isError && isEditing,
            supportingText = if (isError && isEditing && errorMessage.isNotEmpty()) {
                { Text(errorMessage, color = MaterialTheme.colorScheme.error) }
            } else null,
            readOnly = !isEditing
        )
    }
}

@Composable
fun SubmitButton(
    onClick: () -> Unit,
    text: String = "Submit",
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .width(150.dp)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Composable
fun SuccessPopup(
    onDismiss: () -> Unit,
    title: String = "Success!",
    message: String = "Your information has been updated!",
    iconRes: Int = R.drawable.success
) {
    Popup(
        onDismissRequest = onDismiss,
        properties = PopupProperties(focusable = true)
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(Color.White, shape = RoundedCornerShape(8.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = "Success Icon",
                modifier = Modifier
                    .size(80.dp)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(Color.Green)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = Color.Green,
                modifier = Modifier
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                textAlign = TextAlign.Center
            )
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
