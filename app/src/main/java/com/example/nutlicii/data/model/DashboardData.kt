package com.example.nutlicii.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DashboardData(
    val progress_percentage: Int,
    val daily_calories: Int,
    val calories_goal: Int,
    val daily_sugar: Int,
    val daily_salt: Int,
    val daily_fat: Int,
    val bmi: Int,
    val advices: String
) : Parcelable
