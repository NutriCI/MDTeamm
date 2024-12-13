package com.example.nutlicii.data.repository

import com.example.nutlicii.data.model.ApiResponse
import com.example.nutlicii.data.model.FoodRequestAdd
import com.example.nutlicii.data.model.FoodUpdateRequest
import data.Remote.ApiService
import data.local.db.AppDatabase
import data.model.Userdata
import retrofit2.Response
class FoodRepository(
    private val apiService: ApiService,
    private val appDatabase: AppDatabase
) {
    suspend fun addFood(
        nama_makanan: String,
        category: String,
        calories: Int?,
        sugar: Int?,
        fats: Int?,
        salt: Int?,
    ): Response<ApiResponse<Userdata>> {

        val user = appDatabase.userDao().getUser()
        val username = user?.username ?: throw IllegalStateException("User not found")
        val token=user?.token ?:throw IllegalStateException("Token not found")
        val foodAddRequest = FoodRequestAdd(nama_makanan, category, calories, sugar, fats, salt)
        return apiService.addFood(token, username, foodAddRequest)
    }

    suspend fun updateFood(
        id: Int,
        nama_makanan: String,
        category: String,
        calories: Int?,
        sugar: Int?,
        fats: Int?,
        salt: Int?
    ): Response<ApiResponse<Userdata>> {
        val user = appDatabase.userDao().getUser()
        val username = user?.username ?: throw IllegalStateException("User not found")
        val token = user?.token ?: throw IllegalStateException("Token not found")
        val foodUpdateRequest = FoodUpdateRequest(
            nama_makanan = nama_makanan,
            category = category,
            calories = calories,
            sugar = sugar,
            fats = fats,
            salt = salt
        )
        return apiService.updateFood(
            authorization = "Bearer $token",
            username = username,
            foodId = id,
            foodUpdateRequest = foodUpdateRequest
        )
    }
    suspend fun deleteFood(id: Int): Response<ApiResponse<Userdata>> {
        val user = appDatabase.userDao().getUser()
        val username = user?.username ?: throw IllegalStateException("User not found")
        val token = user?.token ?: throw IllegalStateException("Token not found")

        return apiService.deleteFood("Bearer $token", id, username)
    }
}
