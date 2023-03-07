package com.example.starlingtest.ui.transactions.usecases

import com.example.starlingtest.ui.transactions.data.TransactionsRepository
import com.example.starlingtest.ui.transactions.states.TransactionsState
import com.example.starlingtest.utils.networking.NetworkResponse
import com.example.starlingtest.utils.networking.getErrorMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.Date

class RefreshTransactionsUseCase(
    private val transactionsRepository: TransactionsRepository
) {
    suspend operator fun invoke(
        clientStateFlow: MutableStateFlow<TransactionsState>,
        accountUid: String,
        mainWalletUid: String,
        since: Date
    ) {
        val currentClientState = clientStateFlow.value
        // Show loading
        clientStateFlow.update {
            currentClientState.copy(
                transactions = emptyList(),
                isLoading = true
            )
        }
        // Fetch transactions
        when (
            val response = transactionsRepository.fetchTransactions(
                accountUid,
                mainWalletUid,
                since
            )
        ) {
            is NetworkResponse.Success -> {
                clientStateFlow.update {
                    currentClientState.copy(
                        transactions = response.body.items,
                        isLoading = false,
                        error = null
                    )
                }
            }
            else -> {
                clientStateFlow.update {
                    currentClientState.copy(
                        transactions = emptyList(),
                        isLoading = false,
                        error = response.getErrorMessage()
                    )
                }
            }
        }
    }
}
