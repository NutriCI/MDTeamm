package com.example.nutlicii.data.model

data class FoodItemTag(
    val name: String,
    val type: Type
) {
    enum class Type {
        DRINK,
        FREE_SUGAR,
        Makanan,
        Cemilan,
    }
}
