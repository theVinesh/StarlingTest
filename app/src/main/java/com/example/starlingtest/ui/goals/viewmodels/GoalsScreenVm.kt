package com.example.starlingtest.ui.goals.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import com.example.starlingtest.api.ApiFactory
import com.example.starlingtest.ui.accounts.states.AccountsUIState
import com.example.starlingtest.ui.goals.data.GoalsRepository
import com.example.starlingtest.ui.goals.reducers.GoalsStateReducer
import com.example.starlingtest.ui.goals.states.Goal
import com.example.starlingtest.ui.goals.states.GoalsState
import com.example.starlingtest.ui.goals.states.GoalsUiState
import com.example.starlingtest.ui.goals.usecases.RefreshGoalsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GoalsScreenVm(
    private val accountUid: String?,
    private val repository: GoalsRepository = GoalsRepository(ApiFactory().createStarlingTestApi()),
    private val refreshGoalsUseCase: RefreshGoalsUseCase = RefreshGoalsUseCase(repository),
    private val stateReducer: GoalsStateReducer = GoalsStateReducer(),
    val onTap: (Goal) -> Unit
) : ViewModel() {
    private val clientState = MutableStateFlow(
        value = GoalsState(
            goals = emptyList(),
            isLoading = true
        )
    )

    val uiState = clientState.map(stateReducer::computeUiState).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GoalsUiState.Loading
    )

    init {
        refreshGoals()
    }

    fun refreshGoals() {
        viewModelScope.launch(Dispatchers.IO) {
            refreshGoalsUseCase(
                accountUid = accountUid,
                clientState,
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun create(
            owner: ViewModelStoreOwner,
            accountUid: String?,
            onTap: (Goal) -> Unit
        ) = ViewModelProvider(
            owner,
            factory(accountUid, onTap)
        )[GoalsScreenVm::class.java]

        private fun factory(
            accountUid: String?,
            onTap: (Goal) -> Unit
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                GoalsScreenVm(accountUid = accountUid, onTap = onTap) as T
        }
    }
}
