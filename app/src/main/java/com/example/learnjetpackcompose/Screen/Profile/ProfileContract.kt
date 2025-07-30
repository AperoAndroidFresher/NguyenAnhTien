package com.example.learnjetpackcompose.Screen.Profile

data class ProfileState(
    val name: String = "",
    val description: String = "",
    val phoneNumber: String = "",
    val universityName: String = "",
    val imagePath: String? = "",
    val errors: ProfileErrors = ProfileErrors(),
    val isLoading: Boolean = false,
)

data class ProfileErrors(
    val nameError: String? = null,
    val phoneNumberError: String? = null,
    val universityNameError: String? = null
)

sealed interface ProfileIntent{
    data class NameChanged(val name: String): ProfileIntent
    data class DescriptionChanged(val description: String): ProfileIntent
    data class PhoneNumberChanged(val phone: String): ProfileIntent
    data class UniversityNameChanged(val universityName: String): ProfileIntent
    data class ImagePathChanged(val imagePath: String): ProfileIntent
    data object Submit: ProfileIntent
    data object ResetForm: ProfileIntent
}

sealed interface ProfileEffect{
    data object NavigateBack: ProfileEffect
}