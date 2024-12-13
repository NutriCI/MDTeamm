package com.example.nutlicii.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DashboardResponse(
    val data: DashboardData
): Parcelable
