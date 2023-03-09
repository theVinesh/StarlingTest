package com.example.starlingtest.ui.accounts.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starlingtest.ui.accounts.reducers.AccountsStateReducer
import com.example.starlingtest.ui.accounts.states.AccountsState
import com.example.starlingtest.ui.accounts.states.AccountsUIState
import com.example.starlingtest.ui.accounts.usecases.RefreshAccountsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountsScreenVm @Inject constructor(
    private val refreshAccountsUseCase: RefreshAccountsUseCase,
    private val stateReducer: AccountsStateReducer,
) : ViewModel() {
    private val clientState = MutableStateFlow(
        value = AccountsState(
            accounts = emptyList(),
            isLoading = true
        )
    )

    val uiState = clientState.map(stateReducer::computeUiState).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = AccountsUIState.Loading
    )

    init {
        refreshAccounts()
    }

    fun refreshAccounts() {
        viewModelScope.launch(Dispatchers.IO) {
            refreshAccountsUseCase(clientState)
        }
    }
}
