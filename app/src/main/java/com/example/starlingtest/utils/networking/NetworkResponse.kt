package com.example.starlingtest.utils.networking

import java.io.IOException

sealed class NetworkResponse<out T : Any> {
    // Success with response body
    data class Success<T : Any>(val body: T) : NetworkResponse<T>()

    // Failure with error code
    data class ServerError(val code: Int) : NetworkResponse<Nothing>()

    // Network error
    data class NetworkError(val error: IOException) : NetworkResponse<Nothing>()

    // Client error For example, json parsing error
    data class ClientError(val error: Throwable) : NetworkResponse<Nothing>()
}
