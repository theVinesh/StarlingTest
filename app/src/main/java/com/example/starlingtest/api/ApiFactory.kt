package com.example.starlingtest.api

import com.example.starlingtest.utils.networking.NetworkResponseAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class ApiFactory {
    private val okHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(HeaderInterceptor())
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addCallAdapterFactory(NetworkResponseAdapterFactory())
        .addConverterFactory(KotlinXConvertorFactory.getConvertorFactory())
        .build()

    fun createStarlingTestApi(): StarlingTestApi = retrofit.create(StarlingTestApi::class.java)

    private companion object {
        private const val BASE_URL = "https://api-sandbox.starlingbank.com/api/v2/"
    }
}
