package com.example.nutlicii.data.model

data class FoodItem(
    val id: Int,
    val nama_makanan: String,
    val category: String,
    val calories: Int,
    val sugar: Int,
    val fats: Int,
    val salt: Int,
    val date_added: String,
    val grade: String
)
