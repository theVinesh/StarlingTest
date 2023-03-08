package com.example.starlingtest.ui.roundups.reducers

import androidx.annotation.VisibleForTesting
import com.example.starlingtest.ui.roundups.models.Direction
import com.example.starlingtest.ui.roundups.models.TransactionModel
import com.example.starlingtest.ui.roundups.models.TransactionStatus
import com.example.starlingtest.ui.roundups.states.Amount
import com.example.starlingtest.ui.roundups.states.RoundupsState
import com.example.starlingtest.ui.roundups.states.RoundupsUiState
import com.example.starlingtest.ui.roundups.states.Transaction
import com.example.starlingtest.ui.roundups.states.roundUp


class RoundupsStateReducer {
    fun computeUiState(state: RoundupsState): RoundupsUiState = when {
        state.error != null -> RoundupsUiState.Error(state.error)
        state.since == null -> RoundupsUiState.Content.Initial
        state.isLoading -> RoundupsUiState.Loading
        state.transactions.isEmpty() -> RoundupsUiState.Content.NoTransactions
        else -> {
            val outboundTransactions = getSettledOutboundTransactions(state.transactions)
            if (outboundTransactions.isEmpty()) {
                RoundupsUiState.Content.NoTransactions
            } else {
                val roundUpTotal = computeRoundUpTotal(outboundTransactions)
                RoundupsUiState.Content.Transactions(
                    since = state.since,
                    transactionsWithRoundUp = RoundupsUiState.Content.TransactionsWithRoundUp(
                        outboundTransactions = outboundTransactions,
                        roundUpTotal = Amount(
                            (roundUpTotal * 100).toInt(),
                            "GBP" // TODO change this
                        )
                    )
                )
            }
        }
    }

    @VisibleForTesting
    fun getSettledOutboundTransactions(transactions: List<TransactionModel>) =
        transactions
            .filter { it.direction == Direction.OUT && it.status == TransactionStatus.SETTLED }
            .map {
                Transaction(
                    uid = it.uid,
                    sentTo = it.counterPartyName,
                    amount = Amount(it.amount.inMinorUnits, it.amount.currency)
                )
            }

    @VisibleForTesting
    fun computeRoundUpTotal(outboundTransactions: List<Transaction>) =
        outboundTransactions.fold(0f) { acc, transaction ->
            acc + transaction.amount.roundUp().value
        }
}
