package com.example.starlingtest.ui.roundups.data

import com.example.starlingtest.api.StarlingApi
import com.example.starlingtest.ui.roundups.models.TransactionsResponse
import com.example.starlingtest.utils.networking.NetworkResponse
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

class TransactionsRepository @Inject constructor(private val api: StarlingApi) {
    suspend fun fetchTransactions(
        accountUid: String,
        mainWalletUid: String,
        since: LocalDate
    ): NetworkResponse<TransactionsResponse> = api.fetchTransactions(
        accountUid,
        mainWalletUid,
        convertDateToString(since)
    )

    private fun convertDateToString(date: LocalDate): String {
        return "${date}T${LocalTime.MIDNIGHT}Z"
    }
}
