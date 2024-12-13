package com.example.nutlicii.data.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nutlicii.data.Repository.UserProfileRepository

class UserProfileViewModelFactory(
    private val userProfileRepository: UserProfileRepository
) : ViewModelProvider.Factory {

    // The create method should override the one from ViewModelProvider.Factory
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Check if the modelClass is assignable from UserProfileViewModel
        if (modelClass.isAssignableFrom(UserProfileViewModel::class.java)) {
            return UserProfileViewModel(userProfileRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
