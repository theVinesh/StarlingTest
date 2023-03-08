package com.example.starlingtest.ui.transactions.reducers

import com.example.starlingtest.ui.transactions.models.Direction
import com.example.starlingtest.ui.transactions.states.Amount
import com.example.starlingtest.ui.transactions.states.Transaction
import com.example.starlingtest.ui.transactions.states.TransactionsState
import com.example.starlingtest.ui.transactions.states.TransactionsUIState
import com.example.starlingtest.ui.transactions.states.roundUp


class TransactionsStateReducer {

    fun computeUiState(state: TransactionsState): TransactionsUIState {
        if (state.error != null) {
            return TransactionsUIState.Error(state.error)
        }
        if (state.isLoading) {
            return TransactionsUIState.Loading
        }
        return if (state.transactions.isNotEmpty()) {
            val outboundTransactions = state.transactions
                .filter { it.direction == Direction.OUT }
                .map {
                    Transaction(
                        uid = it.uid,
                        sentTo = it.counterPartyName,
                        amount = Amount(it.amount.inMinorUnits, it.amount.currency)
                    )
                }
            val roundUpTotal = outboundTransactions.fold(0f) { acc, transaction ->
                acc + transaction.amount.roundUp().value
            }
            TransactionsUIState.Content(
                since = state.since,
                outboundTransactions = outboundTransactions,
                roundUpTotal = Amount((roundUpTotal * 100).toInt(), "GBP") // TODO change this
            )
        } else {
            TransactionsUIState.Content(
                since = null,
                outboundTransactions = emptyList(),
                roundUpTotal = Amount(0, "GBP") // TODO change this
            )
        }
    }
}
