package com.example.starlingtest.ui.roundups.states

import com.example.starlingtest.ui.roundups.models.TransactionModel
import java.time.LocalDate
import kotlin.math.ceil


data class RoundupsState(
    val since: LocalDate? = null,
    val transactions: List<TransactionModel> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false,
)

sealed interface RoundupsUiState {
    object Loading : RoundupsUiState

    sealed class Content(val since: LocalDate?) : RoundupsUiState {
        object Initial : Content(since = null)
        object NoTransactions : Content(since = null)
        class Transactions(
            since: LocalDate,
            val transactionsWithRoundUp: TransactionsWithRoundUp
        ) : Content(since)

        data class TransactionsWithRoundUp(
            val outboundTransactions: List<Transaction>,
            val roundUpTotal: Amount
        )
    }

    data class Error(val message: String) : RoundupsUiState
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
