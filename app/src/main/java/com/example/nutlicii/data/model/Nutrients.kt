package com.example.nutlicii.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Nutrients(
    val sugar: String,
    val fat: String,
    val salt: String
): Parcelable