package com.example.starlingtest.api

import com.example.starlingtest.ui.accounts.models.AccountsResponse
import com.example.starlingtest.ui.goals.models.CreateGoalParams
import com.example.starlingtest.ui.goals.models.SavingsResponse
import com.example.starlingtest.ui.goals.models.TransferToGoalParams
import com.example.starlingtest.ui.roundups.models.TransactionsResponse
import com.example.starlingtest.utils.networking.NetworkResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface StarlingApi {
    @GET("accounts")
    suspend fun fetchAccounts(): NetworkResponse<AccountsResponse>

    @GET("feed/account/{accountUid}/category/{categoryUid}")
    suspend fun fetchTransactions(
        @Path("accountUid") accountUid: String,
        @Path("categoryUid") mainWalletUid: String,
        @Query("changesSince", encoded = true) changesSince: String
    ): NetworkResponse<TransactionsResponse>

    @GET("account/{accountUid}/savings-goals")
    suspend fun fetchGoals(
        @Path("accountUid") accountUid: String,
    ): NetworkResponse<SavingsResponse>

    @PUT("account/{accountUid}/savings-goals")
    suspend fun createGoal(
        @Path("accountUid") accountUid: String,
        @Body params: CreateGoalParams
    ): NetworkResponse<Unit>

    @PUT("account/{accountUid}/savings-goals/{savingsGoalUid}/add-money/{transferUid}")
    suspend fun transferToGoal(
        @Path("accountUid") accountUid: String,
        @Path("savingsGoalUid") goalUid: String,
        @Path("transferUid") transferUid: String,
        @Body params: TransferToGoalParams
    ): NetworkResponse<Unit>
}
