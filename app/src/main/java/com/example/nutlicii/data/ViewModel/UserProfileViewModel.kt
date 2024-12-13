package com.example.nutlicii.data.ViewModel

import androidx.lifecycle.*
import com.example.nutlicii.data.Repository.UserProfileRepository
import com.example.nutlicii.data.model.UserProfile
import kotlinx.coroutines.launch
import java.io.File

class UserProfileViewModel(private val userProfileRepository: UserProfileRepository) : ViewModel() {
    private val _userProfile = MutableLiveData<UserProfile?>()
    val userProfile: LiveData<UserProfile?> get() = _userProfile

    private val _updateProfileSuccess = MutableLiveData<Boolean>()
    val updateProfileSuccess: LiveData<Boolean> get() = _updateProfileSuccess

    private val _uploadProfilePictureSuccess = MutableLiveData<Boolean>()
    val uploadProfilePictureSuccess: LiveData<Boolean> get() = _uploadProfilePictureSuccess

    fun loadUserProfile() {
        viewModelScope.launch {
            try {
                _userProfile.value = userProfileRepository.getUserProfile()
            } catch (e: Exception) {
                _userProfile.value = null
            }
        }
    }

    fun updateUserProfile(userProfile: UserProfile) {
        viewModelScope.launch {
            try {
                val success = userProfileRepository.updateUserProfile(userProfile)
                if (success) {
                    _userProfile.value = userProfile
                    _updateProfileSuccess.value = true
                } else {
                    _updateProfileSuccess.value = false
                }
            } catch (e: Exception) {
                _updateProfileSuccess.value = false
            }
        }
    }

    fun uploadProfilePicture(file: File) {
        viewModelScope.launch {
            try {
                val success = userProfileRepository.uploadProfilePicture(file)
                if (success) {
                    _uploadProfilePictureSuccess.value = true
                    loadUserProfile()
                } else {
                    _uploadProfilePictureSuccess.value = false
                }
            } catch (e: Exception) {
                _uploadProfilePictureSuccess.value = false
            }
        }
    }
}
