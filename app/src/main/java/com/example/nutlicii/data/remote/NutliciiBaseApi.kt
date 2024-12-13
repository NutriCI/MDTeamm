package data.Remote

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NutliciiBaseApi {
    private const val BASE_URL = "https://nutrici-api-899181208102.asia-southeast2.run.app/"

    fun getApiService(): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d("HttpLog", message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                Log.d("HttpRequest", "URL: ${request.url}, Method: ${request.method}, Headers: ${request.headers}")
                val response = chain.proceed(request)
                Log.d("HttpResponse", "Code: ${response.code}, Message: ${response.message}")
                response
            }
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
