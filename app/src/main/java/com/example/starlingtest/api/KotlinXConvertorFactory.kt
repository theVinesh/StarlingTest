package com.example.starlingtest.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Converter

internal object KotlinXConvertorFactory {
    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

    fun getConvertorFactory(): Converter.Factory {
        val contentType = "application/json".toMediaType()
        return json.asConverterFactory(contentType)
    }
}
