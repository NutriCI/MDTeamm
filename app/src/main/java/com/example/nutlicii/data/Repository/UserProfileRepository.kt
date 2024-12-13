package com.example.nutlicii.data.Repository

import com.example.nutlicii.data.model.UserProfile
import data.Remote.ApiService
import data.local.dao.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UserProfileRepository(private val apiService: ApiService, private val userDao: UserDao) {

    suspend fun getUserProfile(): UserProfile? = withContext(Dispatchers.IO) {
        val user = userDao.getUser()
        user?.let {
            val token = "Bearer ${user.token}"
            val response = apiService.getUserProfile(token, user.username)
            if (response.isSuccessful) {
                response.body()?.data
            } else {
                null
            }
        }
    }

    suspend fun updateUserProfile(userProfile: UserProfile): Boolean = withContext(Dispatchers.IO) {
        val user = userDao.getUser()
        user?.let {
            val token = "Bearer ${user.token}"
            val response = apiService.updateUserProfile(token, user.username, userProfile)
            response.isSuccessful
        } ?: false
    }

    suspend fun uploadProfilePicture(imageFile: File): Boolean = withContext(Dispatchers.IO) {
        val user = userDao.getUser()
        user?.let {
            val token = "Bearer ${user.token}"
            val requestFile: RequestBody = imageFile
                .asRequestBody("image/jpeg".toMediaTypeOrNull())
            val body: MultipartBody.Part = MultipartBody.Part.createFormData(
                "file", imageFile.name, requestFile
            )
            val response = apiService.uploadProfilePicture(token,user.username, body)
            response.isSuccessful
        } ?: false
    }
}
