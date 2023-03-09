package com.example.starlingtest.api

import retrofit2.Retrofit
import javax.inject.Inject

class ApiFactory @Inject constructor(private val retrofit: Retrofit) {
    fun createStarlingTestApi(): StarlingApi = retrofit.create(StarlingApi::class.java)
}
