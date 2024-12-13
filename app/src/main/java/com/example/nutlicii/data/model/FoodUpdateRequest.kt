package com.example.nutlicii.data.model


data class FoodUpdateRequest(
    val nama_makanan: String,
    val category: String,
    val calories: Int?=null,
    val sugar:Int?=null,
    val fats: Int?=null,
    val salt: Int?=null,
)
