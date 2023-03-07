package com.example.starlingtest.ui.accounts.usecases

import com.example.starlingtest.ui.accounts.models.AccountsRepository
import com.example.starlingtest.ui.accounts.states.AccountsState
import com.example.starlingtest.utils.networking.NetworkResponse
import com.example.starlingtest.utils.networking.getErrorMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class RefreshAccountsUseCase(
    private val accountsRepository: AccountsRepository
) {
    suspend operator fun invoke(
        clientStateFlow: MutableStateFlow<AccountsState>
    ) {
        val currentClientState = clientStateFlow.value
        // Show loading
        clientStateFlow.update {
            currentClientState.copy(
                accounts = emptyList(),
                isLoading = true
            )
        }
        // Fetch accounts
        when (val response = accountsRepository.fetchAccounts()) {
            is NetworkResponse.Success -> {
                clientStateFlow.update {
                    currentClientState.copy(
                        accounts = response.body.accounts,
                        isLoading = false,
                        error = null
                    )
                }
            }
            else -> {
                clientStateFlow.update {
                    currentClientState.copy(
                        accounts = emptyList(),
                        isLoading = false,
                        error = response.getErrorMessage()
                    )
                }
            }
        }
    }
}
