package com.example.nutlicii.data.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nutlicii.data.repository.FoodRepository
import com.example.nutlicii.ui.viewmodel.FoodViewModel

class FoodViewModelFactory(
    private val foodRepository: FoodRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FoodViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FoodViewModel(foodRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
