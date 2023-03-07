package com.example.starlingtest.ui.transactions.states

import com.example.starlingtest.ui.transactions.models.TransactionModel
import java.util.Date


data class TransactionsState(
    val since:Date? = null,
    val transactions: List<TransactionModel> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false,
)

sealed interface TransactionsUIState {
    object Loading : TransactionsUIState

    data class Content(
        val outboundTransactions: List<Transaction>,
        val roundUpTotal: Int
    ) : TransactionsUIState

    data class Error(val message: String) : TransactionsUIState
}

data class Transaction(
    val uid: String,
    val amount: Double,
    val amountInMinorUnits: Int,
    val currency: String,
)
