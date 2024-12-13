package com.example.nutlicii.data.repository

import com.example.nutlicii.data.model.PromptRequest
import com.example.nutlicii.data.model.Content
import com.example.nutlicii.data.model.ContentPart
import com.example.nutlicii.data.model.PromptResponse
import data.Remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatRepository(private val apiService: ApiService) {

    private val apiKey = "AIzaSyBK4tfcnTc1IHwA-k95E80ri1ru_Zcdnsc"

    suspend fun generateContent(prompt: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val contentPart = ContentPart(text = prompt)
                val content = Content(parts = listOf(contentPart), role = "user")
                val request = PromptRequest(contents = listOf(content))

                val response = apiService.generateContent(apiKey = apiKey, request = request)

                if (response.isSuccessful && response.body() != null) {
                    val candidate = response.body()?.candidates?.firstOrNull()
                    candidate?.content?.parts?.firstOrNull()?.text ?: "Duhhh,males jawab Ainya."
                } else {
                    "Duhhhh,Ainya lagi gamoon nihhh"
                }
            } catch (e: Exception) {
                "Error occurred: ${e.message}"
            }
        }
    }
}
