package com.example.nutlicii.data.Repository

import android.content.Context
import com.example.nutlicii.data.model.NutritionalInfo
import data.Remote.ApiService
import data.local.db.AppDatabase
import data.model.Userdata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File

class OcrRepository(context: Context, private val apiService: ApiService) {

    private val userDao = AppDatabase.getDatabase(context).userDao()

    suspend fun getUserData(): Userdata? {
        return userDao.getUser()
    }

    suspend fun postImage(imageFile: File, token: String, username: String): Response<NutritionalInfo> {
        val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestBody)

        return withContext(Dispatchers.IO) {
            apiService.uploadOcrImage("Bearer $token", username, imagePart)
        }
    }
}
