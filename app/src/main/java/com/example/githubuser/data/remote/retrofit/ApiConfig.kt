package com.example.githubuser.data.remote.retrofit

import com.example.githubuser.BuildConfig
<<<<<<< Updated upstream
=======
import com.example.githubuser.data.remote.ApiService
>>>>>>> Stashed changes
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        fun getApiService(): ApiService {
            val loggingInterceptor = if(BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            }else{
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }

            val authInterceptor = Interceptor {chain ->
                val req = chain.request()
                val requestHeaders = req.newBuilder()
<<<<<<< Updated upstream
//                    .addHeader("Authorization", BuildConfig.APP_KEY)
=======
                    .addHeader("Authorization",BuildConfig.APP_KEY)
>>>>>>> Stashed changes
                    .build()
                chain.proceed(requestHeaders)
            }

            val client = OkHttpClient
                .Builder()
                .addInterceptor(authInterceptor).build()
//                .addInterceptor(loggingInterceptor).build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}