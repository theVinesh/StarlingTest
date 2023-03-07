package com.example.starlingtest.api

import com.example.starlingtest.ui.accounts.models.AccountsResponse
import com.example.starlingtest.ui.transactions.models.TransactionsResponse
import com.example.starlingtest.utils.networking.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StarlingApi {
    @GET("accounts")
    suspend fun fetchAccounts(): NetworkResponse<AccountsResponse>

    @GET("feed/account/{accountUid}/category/{categoryUid}")
    suspend fun fetchTransactions(
        @Path("accountUid") accountUid: String,
        @Path("categoryUid") mainWalletUid: String,
        @Query("changesSince") changesSince: String
    ): NetworkResponse<TransactionsResponse>
}
