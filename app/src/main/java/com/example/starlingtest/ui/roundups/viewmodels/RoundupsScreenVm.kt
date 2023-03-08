package com.example.starlingtest.ui.roundups.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import com.example.starlingtest.api.ApiFactory
import com.example.starlingtest.ui.roundups.data.TransactionsRepository
import com.example.starlingtest.ui.roundups.reducers.RoundupsStateReducer
import com.example.starlingtest.ui.roundups.states.RoundupsScreenEffects
import com.example.starlingtest.ui.roundups.states.RoundupsState
import com.example.starlingtest.ui.roundups.states.RoundupsUiState
import com.example.starlingtest.ui.roundups.usecases.RefreshTransactionsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class RoundupsScreenVm(
    private val accountUid: String?,
    private val mainWalletUid: String?,
    private val repository: TransactionsRepository = TransactionsRepository(ApiFactory().createStarlingTestApi()),
    private val refreshTransactionsUseCase: RefreshTransactionsUseCase = RefreshTransactionsUseCase(
        repository
    ),
    private val stateReducer: RoundupsStateReducer = RoundupsStateReducer(),
    private val onRoundUp: (Int) -> Unit
) : ViewModel() {
    private val clientState = MutableStateFlow(
        value = RoundupsState(
            transactions = emptyList(),
            isLoading = false
        )
    )

    val uiState = clientState.map(stateReducer::computeUiState).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RoundupsUiState.Loading
    )

    private val _effect = MutableSharedFlow<RoundupsScreenEffects>()
    val effect = _effect.shareIn(viewModelScope, SharingStarted.Eagerly)

    fun fetchTransactions(since: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            refreshTransactionsUseCase(
                clientState,
                accountUid,
                mainWalletUid,
                since
            )
        }
    }

    fun showDatePicker(show: Boolean) {
        viewModelScope.launch {
            _effect.emit(RoundupsScreenEffects.ShowDatePicker(show))
        }
    }

    fun onDateSelected(date: LocalDate) {
        clientState.update {
            it.copy(since = date)
        }
        showDatePicker(false)
        fetchTransactions(date)
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun create(
            owner: ViewModelStoreOwner,
            accountUid: String?,
            mainWalletUid: String?,
            onRoundUp: (Int) -> Unit
        ) = ViewModelProvider(
            owner,
            factory(
                accountUid = accountUid,
                mainWalletUid = mainWalletUid,
                onRoundUp = onRoundUp
            )
        )[RoundupsScreenVm::class.java]

        private fun factory(
            accountUid: String?,
            mainWalletUid: String?,
            onRoundUp: (Int) -> Unit
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                RoundupsScreenVm(
                    accountUid = accountUid,
                    mainWalletUid = mainWalletUid,
                    onRoundUp = onRoundUp
                ) as T
        }
    }
}
