package com.example.nutlicii.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Calories(
    val current: Int,
    val goal: Int
): Parcelable
