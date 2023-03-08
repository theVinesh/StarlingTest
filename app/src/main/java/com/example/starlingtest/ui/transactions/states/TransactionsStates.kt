package com.example.starlingtest.ui.transactions.states

import com.example.starlingtest.ui.transactions.models.TransactionModel
import java.time.LocalDate
import kotlin.math.ceil


data class TransactionsState(
    val since: LocalDate? = null,
    val transactions: List<TransactionModel> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false,
)

sealed interface TransactionsUIState {
    object Loading : TransactionsUIState

    data class Content(
        val since: LocalDate?,
        val outboundTransactions: List<Transaction>,
        val roundUpTotal: Amount
    ) : TransactionsUIState

    data class Error(val message: String) : TransactionsUIState
}

data class Transaction(
    val uid: String,
    val sentTo: String,
    val amount: Amount
)

data class Amount(
    val amountInMinorUnits: Int,
    val currency: String,
) {
    val value = amountInMinorUnits / 100.toFloat()
    val valueString = String.format("%.2f", value)
}

fun Amount.roundUp(): Amount {
    val ceil = ceil(value)
    return Amount(((ceil(value) - value) * 100).toInt(), currency)
}
