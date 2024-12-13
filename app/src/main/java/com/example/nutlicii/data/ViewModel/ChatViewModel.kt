package com.example.nutlicii.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutlicii.data.repository.ChatRepository
import kotlinx.coroutines.launch

class ChatViewModel(private val chatRepository: ChatRepository) : ViewModel() {

    private val _responseMessage = MutableLiveData<String>()
    val responseMessage: LiveData<String> get() = _responseMessage

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun sendMessage(prompt: String) {
        viewModelScope.launch {
            try {
                val response = chatRepository.generateContent(prompt)
                _responseMessage.postValue(response)
            } catch (e: Exception) {

                _errorMessage.postValue("Error: ${e.message}")
            }
        }
    }
}
