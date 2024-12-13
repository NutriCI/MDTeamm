package com.example.nutlicii.data.ViewModel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nutlicii.data.repository.AuthRepository

class AuthViewModelFactory(
    private val loginRepository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(loginRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
