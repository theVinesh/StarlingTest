package com.example.starlingtest.ui.accounts.reducers

import com.example.starlingtest.ui.accounts.states.Account
import com.example.starlingtest.ui.accounts.states.AccountsState
import com.example.starlingtest.ui.accounts.states.AccountsUIState


class AccountsStateReducer {
    fun computeUiState(state: AccountsState): AccountsUIState {
        if (state.error != null) {
            return AccountsUIState.Error(state.error)
        }
        if (state.isLoading) {
            return AccountsUIState.Loading
        }
        return if (state.accounts.isNotEmpty()) {
            AccountsUIState.Content(
                state.accounts.map {
                    Account(
                        it.accountUid, it.defaultCategory, it.currency, it.name
                    )
                }
            )
        } else {
            AccountsUIState.Error("No accounts found")
        }
    }
}
