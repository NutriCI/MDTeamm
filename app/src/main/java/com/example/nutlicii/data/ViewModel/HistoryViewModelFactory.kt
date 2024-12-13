package com.example.nutlicii.data.ViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nutlicii.data.Repository.HistoryRepository
import data.Remote.ApiService
import data.local.dao.UserDao

class HistoryViewModelFactory(
    private val application: Application,
    private val apiService: ApiService,
    private val userDao: UserDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            val repository = HistoryRepository(apiService, userDao)
            return HistoryViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
