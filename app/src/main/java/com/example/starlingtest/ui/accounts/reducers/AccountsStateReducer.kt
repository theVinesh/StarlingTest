package com.example.starlingtest.ui.accounts.reducers

import com.example.starlingtest.ui.accounts.states.Account
import com.example.starlingtest.ui.accounts.states.AccountsState
import com.example.starlingtest.ui.accounts.states.AccountsUIState


class AccountsStateReducer {
    fun computeUiState(state: AccountsState): AccountsUIState = when {
        state.error != null -> AccountsUIState.Error(state.error)
        state.isLoading -> AccountsUIState.Loading
        state.accounts.isNotEmpty() -> AccountsUIState.Content(
            state.accounts.map {
                Account(
                    it.accountUid, it.defaultCategory, it.currency, it.name
                )
            }
        )
        else -> AccountsUIState.Error("No accounts found")
    }
}
