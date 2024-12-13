package data.Remote

import androidx.room.Delete
import com.example.nutlicii.data.model.ApiFoodResponse
import retrofit2.http.Query
import com.example.nutlicii.data.model.ApiResponse
import com.example.nutlicii.data.model.DashboardResponse
import com.example.nutlicii.data.model.FoodItem
import com.example.nutlicii.data.model.FoodRequestAdd
import com.example.nutlicii.data.model.FoodUpdateRequest
import com.example.nutlicii.data.model.NutritionalInfo
import com.example.nutlicii.data.model.ProfileResponse
import com.example.nutlicii.data.model.PromptRequest
import com.example.nutlicii.data.model.PromptResponse
import com.example.nutlicii.data.model.UserProfile
import data.model.LoginRequest
import data.model.RegisterRequest
import data.model.Userdata
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @POST("api/users/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<ApiResponse<Userdata>>
    @POST("api/users")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<ApiResponse<Userdata>>
    @GET("api/dashboard/{username}")
    suspend fun getDashboardData(
        @Header("Authorization") authorization: String,
        @Path("username") username: String,
        @Query("date") date: String
    ): DashboardResponse
    @POST("api/food/{username}")
    suspend fun addFood(
        @Header("Authorization") authorization: String,
        @Path("username") username: String,
        @Body food: FoodRequestAdd
    ): Response<ApiResponse<Userdata>>
    @PATCH("api/food/{foodId}&{username}")
        suspend fun updateFood(
            @Header("Authorization") authorization: String,
            @Path("foodId") foodId: Int,
            @Path("username") username: String,
            @Body foodUpdateRequest: FoodUpdateRequest
        ): Response<ApiResponse<Userdata>>
    @DELETE("api/food/{foodId}&{username}")
    suspend fun deleteFood(
        @Header("Authorization") authorization: String,
        @Path("foodId") foodId: Int,
        @Path("username") username: String
    ): Response<ApiResponse<Userdata>>
    @Headers("Content-Type: application/json")
    @POST("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: PromptRequest
    ): Response<PromptResponse>
    @GET("api/profile/{username}")
    suspend fun getUserProfile(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Response<ProfileResponse<UserProfile>>
    @POST("api/profile/{username}")
    suspend fun updateUserProfile(
        @Header("Authorization") token: String,
        @Path("username") username: String,
        @Body userProfile: UserProfile
    ): Response<UserProfile>
    @Multipart
    @POST("api/food/ocr/{username}")
    suspend fun uploadOcrImage(
        @Header("Authorization") authorization: String,
        @Path("username") username: String,
        @Part image: MultipartBody.Part
    ): Response<NutritionalInfo>
    @Multipart
    @PATCH("api/profile/{username}")
    suspend fun uploadProfilePicture(
        @Header("Authorization") authorization: String,
        @Path("username") username: String,
        @Part profilePicture: MultipartBody.Part
    ): Response<Unit>
    @GET("api/food/{username}")
    suspend fun getFoodsByDate(
        @Header("Authorization") token: String,
        @Path("username") username: String,
        @Query("date") date: String
    ): Response<ApiFoodResponse<FoodItem>>
}

