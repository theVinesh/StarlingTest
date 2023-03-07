package com.example.starlingtest.api

import com.example.starlingtest.ui.accounts.models.AccountsResponse
import com.example.starlingtest.utils.networking.NetworkResponse
import retrofit2.http.GET

interface StarlingApi {
    @GET("accounts")
    suspend fun fetchAccounts(): NetworkResponse<AccountsResponse>
}
