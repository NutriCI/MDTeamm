package com.example.nutlicii.data.model

data class HistoryItem(
    val id:Int,
    val date_added: String,
    val nama_makanan: String,
    val category: String,
    val grade: String,
    val calories:Int,
    val sugar:Int,
    val fats:Int,
    val salt:Int,
)
