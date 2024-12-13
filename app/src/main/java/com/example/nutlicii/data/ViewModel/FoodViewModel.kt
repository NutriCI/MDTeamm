package com.example.nutlicii.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutlicii.data.model.ApiResponse
import com.example.nutlicii.data.repository.FoodRepository
import data.model.Userdata
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.Date

class FoodViewModel(
    private val foodRepository: FoodRepository
) : ViewModel() {

    fun addFood(
        nama_makanan: String,
        category: String,
        calories: Int?,
        sugar: Int?,
        fats: Int?,
        salt: Int?,
        onSuccess: (Response<ApiResponse<Userdata>>) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = foodRepository.addFood(
                    nama_makanan = nama_makanan,
                    category = category,
                    calories = calories,
                    sugar = sugar,
                    fats = fats,
                    salt = salt,
                )
                if (response.isSuccessful) {
                    onSuccess(response)
                } else {
                    onError("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Unknown error occurred")
            }
        }
    }
    // Fungsi untuk update makanan
    fun updateFood(
        id: Int,
        nama_makanan: String,
        category: String,
        calories: Int?,
        sugar: Int?,
        fats: Int?,
        salt: Int?,
        onSuccess: (Response<ApiResponse<Userdata>>) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Panggil repository untuk melakukan update
                val response = foodRepository.updateFood(
                    id, nama_makanan, category, calories, sugar, fats, salt
                )

                // Periksa jika respons sukses
                if (response.isSuccessful) {
                    onSuccess(response)
                } else {
                    onError("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Unknown error occurred")
            }
        }
    }
    fun deleteFood(
        id: Int,
        onSuccess: (Response<ApiResponse<Userdata>>) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Panggil repository untuk menghapus makanan
                val response = foodRepository.deleteFood(id)

                if (response.isSuccessful) {
                    onSuccess(response)
                } else {
                    onError("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Unknown error occurred")
            }
        }
    }
}
