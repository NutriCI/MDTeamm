package com.example.nutlicii.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Progress(
    val percentage: Int,
    val date: String,
    val calories: Calories
): Parcelable
