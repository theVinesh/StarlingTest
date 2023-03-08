package com.example.starlingtest.ui.accounts.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import com.example.starlingtest.api.ApiFactory
import com.example.starlingtest.ui.accounts.data.AccountsRepository
import com.example.starlingtest.ui.accounts.reducers.AccountsStateReducer
import com.example.starlingtest.ui.accounts.states.Account
import com.example.starlingtest.ui.accounts.states.AccountsState
import com.example.starlingtest.ui.accounts.states.AccountsUIState
import com.example.starlingtest.ui.accounts.usecases.RefreshAccountsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AccountsScreenVm(
    private val repository: AccountsRepository = AccountsRepository(ApiFactory().createStarlingTestApi()),
    private val refreshAccountsUseCase: RefreshAccountsUseCase = RefreshAccountsUseCase(repository),
    private val stateReducer: AccountsStateReducer = AccountsStateReducer(),
    val onTap: (Account) -> Unit
) : ViewModel() {
    private val clientState = MutableStateFlow(
        value = AccountsState(
            accounts = emptyList(),
            isLoading = true
        )
    )

    val uiState = clientState.map(stateReducer::computeUiState).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
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

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun create(
            owner: ViewModelStoreOwner,
            onTap: (Account) -> Unit
        ) = ViewModelProvider(
            owner,
            factory(onTap)
        )[AccountsScreenVm::class.java]

        private fun factory(
            onTap: (Account) -> Unit
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                AccountsScreenVm(onTap = onTap) as T
        }
    }
}
