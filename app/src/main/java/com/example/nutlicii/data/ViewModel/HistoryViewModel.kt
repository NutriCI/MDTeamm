package com.example.nutlicii.data.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nutlicii.data.Repository.HistoryRepository
import com.example.nutlicii.data.model.HistoryItem
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application, private val historyRepository: HistoryRepository) : AndroidViewModel(application) {

    val historyItems = MutableLiveData<List<HistoryItem>>()

    // Function to fetch history items for a given date
    fun getHistoryItemsByDate(date: String) {
        viewModelScope.launch {
            val items = historyRepository.getHistoryItemsByDate(date)
            historyItems.postValue(items)
        }
    }
}
