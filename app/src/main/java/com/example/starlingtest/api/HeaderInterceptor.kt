package com.example.starlingtest.api

import com.example.starlingtest.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
            .header(ACCEPT, CONTENT_TYPE)
            .header(AUTHORIZATION, "$BEARER $TOKEN")

        val request: Request = requestBuilder.build()
        return chain.proceed(request)
    }

    private companion object {
        const val AUTHORIZATION = "Authorization"
        const val ACCEPT = "Accept"
        const val BEARER = "Bearer"
        const val CONTENT_TYPE = "application/json"
        const val TOKEN = BuildConfig.ACCESS_TOKEN
    }
}
