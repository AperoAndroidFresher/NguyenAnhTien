package com.example.learnjetpackcompose.Screen.Profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnjetpackcompose.Utils.ValidationUtils
import com.example.learnjetpackcompose.data.model.UserManager
import com.example.learnjetpackcompose.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    private val _effect = Channel<ProfileEffect>()
    val effect = _effect.receiveAsFlow()

    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    init {
        loadUserData()
    }

    fun processIntent(intent: ProfileIntent){
        viewModelScope.launch{
            when(intent){
                is ProfileIntent.DescriptionChanged -> {
                    _state.update{it.copy(description = intent.description)}
                }
                is ProfileIntent.DisplayNameChanged -> {
                    _state.update{it.copy(displayName = intent.displayName, errors = it.errors.copy(displayNameError = null))}
                }
                is ProfileIntent.PhoneNumberChanged -> {
                    _state.update{it.copy(phoneNumber = intent.phone, errors = it.errors.copy(phoneNumberError = null))}
                }
                is ProfileIntent.ImagePathChanged -> {
                    _state.update{it.copy(imagePath = intent.imagePath)}
                }
                ProfileIntent.ResetForm -> {
                    loadUserData()
                }
                ProfileIntent.Submit -> {
                    validateAndSubmit()
                }
                is ProfileIntent.UniversityNameChanged -> {
                    _state.update{it.copy(universityName = intent.universityName, errors = it.errors.copy(universityNameError = null))}
                }
                ProfileIntent.LoadUserData -> {
                    loadUserData()
                }
                ProfileIntent.Logout -> logout()
            }
        }
    }

    private fun logout(){
        viewModelScope.launch(Dispatchers.IO){
            UserManager.clearCurrentUserId()
            with(sharedPreferences.edit()) {
                remove("is_logged_in")
                remove("user_id")
                apply()
            }
            _effect.send(ProfileEffect.NavigateToLogin)
        }
    }
    private fun loadUserData() {
        viewModelScope.launch(Dispatchers.IO){
            try {
                _state.update { it.copy(isLoading = true) }
                val userId = UserManager.getCurrentUserId()
                val user = userRepository.getUserById(userId)

                if (user != null) {
                    _state.update {
                        it.copy(
                            displayName = user.displayName,
                            description = user.description,
                            phoneNumber = user.phoneNumber,
                            universityName = user.universityName,
                            imagePath = user.avatarPath,
                            currentUser = user,
                            isLoading = false
                        )
                    }
                } else {
                    _effect.send(ProfileEffect.ShowError("User not found"))
                    _state.update { it.copy(isLoading = false) }
                }
            } catch (e: Exception) {
                _effect.send(ProfileEffect.ShowError("Error loading user data: ${e.message}"))
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun validateAndSubmit(){
        val currentState = _state.value
        val validationErrors = ProfileErrors(
            displayNameError = ValidationUtils.validateName(currentState.displayName),
            phoneNumberError = ValidationUtils.validatePhoneNumber(currentState.phoneNumber),
            universityNameError = ValidationUtils.validateUniversity(currentState.universityName)
        )

        val isValid = with(validationErrors){
            displayNameError == null && phoneNumberError == null && universityNameError == null
        }

        if(isValid){
            viewModelScope.launch(Dispatchers.IO){
                try {
                    _state.update{it.copy(isLoading = true)}

                    val currentUser = _state.value.currentUser
                    if (currentUser != null) {
                        val updatedUser = currentUser.copy(
                            displayName = _state.value.displayName,
                            phoneNumber = _state.value.phoneNumber,
                            universityName = _state.value.universityName,
                            description = _state.value.description,
                            avatarPath = _state.value.imagePath ?: ""
                        )

                        userRepository.updateUser(updatedUser)
                        _state.update{it.copy(isLoading = false, currentUser = updatedUser)}
                        _effect.send(ProfileEffect.ProfileSaved)
                        _effect.send(ProfileEffect.NavigateBack)
                    } else {
                        _effect.send(ProfileEffect.ShowError("User not found"))
                        _state.update{it.copy(isLoading = false)}
                    }
                } catch (e: Exception) {
                    _effect.send(ProfileEffect.ShowError("Error saving profile: ${e.message}"))
                    _state.update{it.copy(isLoading = false)}
                }
            }
        } else{
            _state.update{it.copy(errors = validationErrors)}
        }
    }
}
