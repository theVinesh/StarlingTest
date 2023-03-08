package com.example.starlingtest.ui.roundups.usecases

import com.example.starlingtest.ui.roundups.data.TransactionsRepository
import com.example.starlingtest.ui.roundups.states.RoundupsState
import com.example.starlingtest.utils.networking.NetworkResponse
import com.example.starlingtest.utils.networking.getErrorMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class RefreshTransactionsUseCase(
    private val transactionsRepository: TransactionsRepository
) {
    suspend operator fun invoke(
        clientStateFlow: MutableStateFlow<RoundupsState>,
        accountUid: String?,
        mainWalletUid: String?,
        since: LocalDate
    ) {
        val currentClientState = clientStateFlow.value

        if (accountUid == null || mainWalletUid == null) {
            clientStateFlow.update {
                currentClientState.copy(
                    transactions = emptyList(),
                    error = "Invalid account or wallet id",
                    isLoading = false
                )
            }
            return
        }
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
