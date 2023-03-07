package com.example.starlingtest.ui.accounts.states

import com.example.starlingtest.ui.accounts.models.AccountModel


data class AccountsState(
    val accounts: List<AccountModel> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false,
)

sealed interface AccountsUIState {
    object Loading : AccountsUIState

    data class Content(
        val accounts: List<Account>
    ) : AccountsUIState

    data class Error(val message: String) : AccountsUIState
}


data class Account(
    val accountUid: String,
    val mainAccountUid: String,
    val currency: String,
    val name: String,
)
