package com.example.learnjetpackcompose.Screen.Profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnjetpackcompose.Utils.ValidationUtils
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    private val _effect = Channel<ProfileEffect>()
    val effect = _effect.receiveAsFlow()

    fun processIntent(intent: ProfileIntent){
        viewModelScope.launch{
            when(intent){
                is ProfileIntent.DescriptionChanged -> {
                    _state.update{it.copy(description = intent.description)}
                }
                is ProfileIntent.NameChanged -> {
                    _state.update{it.copy(name = intent.name, errors = it.errors.copy(nameError = null))}
                }
                is ProfileIntent.PhoneNumberChanged -> {
                    _state.update{it.copy(phoneNumber = intent.phone, errors = it.errors.copy(phoneNumberError = null))}
                }
                is ProfileIntent.ImagePathChanged -> {
                    _state.update{it.copy(imagePath = intent.imagePath)}
                }
                ProfileIntent.ResetForm -> {
                    _state.value = ProfileState()
                }
                ProfileIntent.Submit -> {
                    validateAndSubmit()
                }
                is ProfileIntent.UniversityNameChanged -> {
                    _state.update{it.copy(universityName = intent.universityName, errors = it.errors.copy(universityNameError = null))}
                }
            }
        }
    }

    private fun validateAndSubmit(){
        val currentState = _state.value
        val validationErrors = ProfileErrors(
            nameError = ValidationUtils.validateName(currentState.name),
            phoneNumberError = ValidationUtils.validatePhoneNumber(currentState.phoneNumber),
            universityNameError = ValidationUtils.validateUniversity(currentState.universityName)
        )

        val isValid = with(validationErrors){
            nameError == null && phoneNumberError == null && universityNameError == null
        }

        if(isValid){
            viewModelScope.launch{
                _state.update{it.copy(isLoading = true)}
                _state.update{it.copy(isLoading = false)}
                _effect.send(ProfileEffect.NavigateBack)
            }
        } else{
            _state.update{it.copy(errors = validationErrors)}
        }
    }

}
