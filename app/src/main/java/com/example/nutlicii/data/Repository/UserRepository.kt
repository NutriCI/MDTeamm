package com.example.nutlicii.data.Repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nutlicii.data.model.DashboardResponse
import data.Remote.ApiService
import com.example.nutlicii.data.utils.Result
import data.local.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class UserRepository(private val context: Context, private val apiService: ApiService) {

    private suspend fun getUserFromRoom(): Pair<String?, String?> {
        val userDao = AppDatabase.getDatabase(context).userDao()
        val user = userDao.getUser()
        return Pair(user?.username, user?.token)
    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
    suspend fun getDashboardData(): LiveData<Result<DashboardResponse>> {
        val liveData = MutableLiveData<Result<DashboardResponse>>()
        liveData.value = Result.Loading

        val (username, token) = getUserFromRoom()
        val date = getCurrentDate()
        if (username == null || token == null) {
            liveData.value = Result.Error("Duhhh,ada yang salah nihhh")
            return liveData
        }

        try {
            val response = withContext(Dispatchers.IO) {
                apiService.getDashboardData("Bearer $token", username, date)
            }
            liveData.value = Result.Success(response)
        } catch (e: Exception) {
            liveData.value = Result.Error("Duhhh,ada yang salah nihhh")
        }
        return liveData
    }
}
