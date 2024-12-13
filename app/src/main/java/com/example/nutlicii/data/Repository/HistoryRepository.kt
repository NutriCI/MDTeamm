package com.example.nutlicii.data.Repository

import com.example.nutlicii.data.model.HistoryItem
import data.Remote.ApiService
import data.local.dao.UserDao

class HistoryRepository(
    private val apiService: ApiService,
    private val userDao: UserDao
) {
    suspend fun getHistoryItemsByDate(date: String): List<HistoryItem> {
        val user = userDao.getUser()
        if (user != null) {
            val response = apiService.getFoodsByDate("Bearer ${user.token}", user.username, date)
            return if (response.isSuccessful) {
                val foodItems = response.body()?.data?.map { foodItem ->
                    HistoryItem(
                        id = foodItem.id,
                        calories = foodItem.calories,
                        fats = foodItem.fats,
                        salt = foodItem.salt,
                        sugar = foodItem.sugar,
                        date_added = foodItem.date_added,
                        nama_makanan = foodItem.nama_makanan,
                        category = foodItem.category,
                        grade = foodItem.grade,
                    )
                }
                foodItems ?: emptyList()
            } else {
                emptyList()
            }
        }
        return emptyList()
    }
}
