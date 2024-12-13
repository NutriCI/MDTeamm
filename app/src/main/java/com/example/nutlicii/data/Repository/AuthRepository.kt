package com.example.nutlicii.data.repository

import com.example.nutlicii.data.model.ApiResponse
import data.Remote.ApiService
import data.local.db.AppDatabase
import data.model.LoginRequest
import data.model.RegisterRequest
import data.model.Userdata
import retrofit2.Response

class AuthRepository(
    private val apiService: ApiService,
    private val appDatabase: AppDatabase
) {

    suspend fun login(username: String, password: String): Response<ApiResponse<Userdata>> {
        val loginRequest = LoginRequest(username, password)
        return apiService.login(loginRequest)
    }
    suspend fun register(username: String, password: String, email: String,name:String,repeatPassword:String): Response<ApiResponse<Userdata>> {
        val registerRequest = RegisterRequest(username, password, repeatPassword, name, email)
        return apiService.register(registerRequest)
    }

    suspend fun saveUserToDb(user: Userdata) {
        appDatabase.userDao().insertUser(user)
    }
    suspend fun Delete(username: String) {
        appDatabase.userDao().Delete(username)
    }
}
