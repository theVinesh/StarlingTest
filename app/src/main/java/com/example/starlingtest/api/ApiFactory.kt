package com.example.starlingtest.api

import com.example.starlingtest.utils.networking.NetworkResponseAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class ApiFactory {
    private val okHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(HeaderInterceptor())
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addCallAdapterFactory(NetworkResponseAdapterFactory())
        .addConverterFactory(KotlinXConvertorFactory.getConvertorFactory())
        .build()

    fun createStarlingTestApi(): StarlingApi = retrofit.create(StarlingApi::class.java)

    private companion object {
        private const val BASE_URL = "https://api-sandbox.starlingbank.com/api/v2/"
    }
}
