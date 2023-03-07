package com.example.starlingtest.utils.networking

fun NetworkResponse<Any>.getErrorMessage(): String = when (this) {
    is NetworkResponse.ClientError -> "Something went wrong : ${error.message}"
    is NetworkResponse.NetworkError -> "Cannot connect to the internet: ${error.message}"
    is NetworkResponse.ServerError -> "Something went wrong on the server: $code"
    else -> "This isn't supposed to happen"
}
