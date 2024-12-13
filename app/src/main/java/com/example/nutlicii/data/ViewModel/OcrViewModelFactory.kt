package com.example.nutlicii.data.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nutlicii.data.Repository.OcrRepository
import data.Remote.ApiService

class OcrViewModelFactory(private val context: Context, private val apiService: ApiService) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OcrViewModel::class.java)) {
            return OcrViewModel(OcrRepository(context, apiService)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
