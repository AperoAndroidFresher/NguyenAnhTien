package com.example.learnjetpackcompose.Screen.Profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel : ViewModel() {

    // State cho các input
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber

    private val _universityName = MutableStateFlow("")
    val universityName: StateFlow<String> = _universityName

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    // Trạng thái lỗi
    private val _nameError = MutableStateFlow<String?>(null)
    val nameError: StateFlow<String?> = _nameError

    private val _phoneError = MutableStateFlow<String?>(null)
    val phoneError: StateFlow<String?> = _phoneError

    private val _universityError = MutableStateFlow<String?>(null)
    val universityError: StateFlow<String?> = _universityError

    // Popup trạng thái thành công
    private val _showSuccessPopup = MutableStateFlow(false)
    val showSuccessPopup: StateFlow<Boolean> = _showSuccessPopup

    // Regex validation
    private val nameRegex = "^[\\p{L} ]{1,30}$".toRegex()
    private val phoneRegex = "^[0-9]{9,15}$".toRegex()
    private val universityRegex = "^[\\p{L} .'-]+$".toRegex()

    // Input update
    fun onNameChange(value: String) {
        _name.value = value
        _nameError.value = null
    }

    fun onPhoneChange(value: String) {
        _phoneNumber.value = value
        _phoneError.value = null
    }

    fun onUniversityChange(value: String) {
        _universityName.value = value
        _universityError.value = null
    }

    fun onDescriptionChange(value: String) {
        _description.value = value
    }

    fun validateAndSubmit(onSuccess: () -> Unit) {
        _nameError.value = if (!nameRegex.matches(_name.value)) "Chỉ chữ và tối đa 30 ký tự" else null
        _phoneError.value = if (!phoneRegex.matches(_phoneNumber.value)) "Chỉ được nhập số từ 9-15 chữ số" else null
        _universityError.value = if (!universityRegex.matches(_universityName.value)) "Không hợp lệ" else null

        val isValid = listOf(_nameError.value, _phoneError.value, _universityError.value).all { it == null }

        if (isValid) {
            _showSuccessPopup.value = true
            onSuccess()
        }
    }

    fun dismissPopup() {
        _showSuccessPopup.value = false
    }

    fun resetAll() {
        _name.value = ""
        _phoneNumber.value = ""
        _universityName.value = ""
        _description.value = ""
        _nameError.value = null
        _phoneError.value = null
        _universityError.value = null
    }
}
