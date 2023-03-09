package com.example.starlingtest.ui.roundups.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.starlingtest.ui.roundups.reducers.RoundupsStateReducer
import com.example.starlingtest.ui.roundups.states.RoundupsScreenEffects
import com.example.starlingtest.ui.roundups.states.RoundupsState
import com.example.starlingtest.ui.roundups.states.RoundupsUiState
import com.example.starlingtest.ui.roundups.usecases.RefreshTransactionsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
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

class RoundupsScreenVm @AssistedInject constructor(
    @Assisted("accountUid") private val accountUid: String?,
    @Assisted("mainWalletUid") private val mainWalletUid: String?,
    private val refreshTransactionsUseCase: RefreshTransactionsUseCase,
    private val stateReducer: RoundupsStateReducer,
) : ViewModel() {
    private val clientState = MutableStateFlow(
        value = RoundupsState(
            transactions = emptyList(),
            isLoading = false
        )
    )

    val uiState = clientState.map(stateReducer::computeUiState).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
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

    @AssistedFactory
    interface RoundupsScreenVmAssistedFactory {
        fun create(
            @Assisted("accountUid") accountUid: String?,
            @Assisted("mainWalletUid") mainWalletUid: String?,
        ): RoundupsScreenVm
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun factory(
            assistedFactory: RoundupsScreenVmAssistedFactory,
            accountUid: String?,
            mainWalletUid: String?,
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                assistedFactory.create(accountUid, mainWalletUid) as T
        }
    }
}
