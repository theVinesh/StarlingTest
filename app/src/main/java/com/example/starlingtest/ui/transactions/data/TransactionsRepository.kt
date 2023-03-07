package com.example.starlingtest.ui.transactions.data

import com.example.starlingtest.api.StarlingApi
import com.example.starlingtest.ui.transactions.models.TransactionsResponse
import com.example.starlingtest.utils.networking.NetworkResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionsRepository(private val api: StarlingApi) {

    suspend fun fetchTransactions(
        accountUid: String,
        mainWalletUid: String,
        since: Date
    ): NetworkResponse<TransactionsResponse> = api.fetchTransactions(
        accountUid,
        mainWalletUid,
        convertDateToString(since)
    )

    private fun convertDateToString(date: Date): String {
        val formatter = SimpleDateFormat("yyyy-MM-ddTHH:mm:ss.SSSZ", Locale.getDefault())
        return formatter.format(date)
    }
}
