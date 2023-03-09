package com.example.starlingtest.ui.roundups.states

import android.os.Parcelable
import com.example.starlingtest.ui.roundups.models.TransactionModel
import kotlinx.parcelize.Parcelize
import java.time.LocalDate


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

@Parcelize
data class Amount(
    val amountInMinorUnits: Long,
    val currency: String,
) : Parcelable {
    override fun toString() = "${String.format("%.2f", amountInMinorUnits / 100f)} $currency"

}


fun Long.roundUp(): Long {
    val ceilAmountInMinorUnits = ((this + 100) / 100) * 100
    return ceilAmountInMinorUnits - this
}

fun Long.toAmount(currency: String) = Amount(this, currency)
