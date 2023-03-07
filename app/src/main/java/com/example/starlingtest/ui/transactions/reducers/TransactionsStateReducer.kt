package com.example.starlingtest.ui.transactions.reducers

import com.example.starlingtest.ui.transactions.models.Direction
import com.example.starlingtest.ui.transactions.states.Transaction
import com.example.starlingtest.ui.transactions.states.TransactionsState
import com.example.starlingtest.ui.transactions.states.TransactionsUIState
import kotlin.math.ceil


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
                        it.uid,
                        it.amount.inMinorUnits / 100.toDouble(),
                        it.amount.inMinorUnits,
                        it.amount.currency
                    )
                }
            val roundUpTotal = outboundTransactions.sumOf { ceil(it.amount) - it.amount }
            val roundUpTotalInMinorUnits = (roundUpTotal * 100).toInt()
            TransactionsUIState.Content(
                outboundTransactions = outboundTransactions,
                roundUpTotal = roundUpTotalInMinorUnits
            )
        } else {
            TransactionsUIState.Error("No transactions found")
        }
    }
}
