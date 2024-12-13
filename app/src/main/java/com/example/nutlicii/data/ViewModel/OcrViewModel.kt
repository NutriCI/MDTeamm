package com.example.nutlicii.data.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutlicii.data.Repository.OcrRepository
import com.example.nutlicii.data.model.NutritionalInfo
import data.model.Userdata
import kotlinx.coroutines.launch
import java.io.File

class OcrViewModel(private val ocrRepository: OcrRepository) : ViewModel() {

    val nutritionalInfo = MutableLiveData<NutritionalInfo>()
    val uploadResult = MutableLiveData<Boolean>()

    // Mengambil data pengguna dari database
    suspend fun getUserDataFromDatabase(): Userdata? {
        return ocrRepository.getUserData()
    }

    // Meng-upload gambar menggunakan token dan username
    fun uploadImage(file: File, token: String, username: String) {
        viewModelScope.launch {
            try {
                val response = ocrRepository.postImage(file, token, username)
                if (response.isSuccessful) {
                    nutritionalInfo.postValue(response.body())  // Update dengan data nutrisi
                    uploadResult.postValue(true)
                } else {
                    uploadResult.postValue(false)
                }
            } catch (e: Exception) {
                uploadResult.postValue(false)
                Log.e("OcrViewModel", "Error uploading image", e)
            }
        }
    }
}
