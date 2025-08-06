package com.example.learnjetpackcompose.Screen.Profile

import com.example.learnjetpackcompose.RoomDB.Entity.User

data class ProfileState(
    val displayName: String = "",
    val description: String = "",
    val phoneNumber: String = "",
    val universityName: String = "",
    val imagePath: String? = "",
    val errors: ProfileErrors = ProfileErrors(),
    val isLoading: Boolean = false,
    val currentUser: User? = null
)

data class ProfileErrors(
    val displayNameError: String? = null,
    val phoneNumberError: String? = null,
    val universityNameError: String? = null
)

sealed interface ProfileIntent{
    data class DisplayNameChanged(val displayName: String): ProfileIntent
    data class DescriptionChanged(val description: String): ProfileIntent
    data class PhoneNumberChanged(val phone: String): ProfileIntent
    data class UniversityNameChanged(val universityName: String): ProfileIntent
    data class ImagePathChanged(val imagePath: String): ProfileIntent
    data object Submit: ProfileIntent
    data object ResetForm: ProfileIntent
    data object LoadUserData: ProfileIntent
}

sealed interface ProfileEffect{
    data object NavigateBack: ProfileEffect
    data object ProfileSaved: ProfileEffect
    data class ShowError(val message: String): ProfileEffect
}