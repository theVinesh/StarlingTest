package com.example.starlingtest.ui.goals.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import com.example.starlingtest.api.ApiFactory
import com.example.starlingtest.ui.goals.data.GoalsRepository
import com.example.starlingtest.ui.goals.models.CreateGoalParams
import com.example.starlingtest.ui.goals.reducers.CreateGoalDialogStateReducer
import com.example.starlingtest.ui.goals.reducers.GoalsStateReducer
import com.example.starlingtest.ui.goals.states.CreateGoalDialogState
import com.example.starlingtest.ui.goals.states.CreateGoalDialogUiState
import com.example.starlingtest.ui.goals.states.Goal
import com.example.starlingtest.ui.goals.states.GoalsScreenEffects
import com.example.starlingtest.ui.goals.states.GoalsState
import com.example.starlingtest.ui.goals.states.GoalsUiState
import com.example.starlingtest.ui.goals.usecases.CreateGoalUseCase
import com.example.starlingtest.ui.goals.usecases.RefreshGoalsUseCase
import com.example.starlingtest.ui.goals.usecases.TransferToGoalUseCase
import com.example.starlingtest.ui.roundups.states.Amount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GoalsScreenVm(
    private val accountUid: String?,
    private val roundUpToTransfer: Amount?,
    private val repository: GoalsRepository = GoalsRepository(ApiFactory().createStarlingTestApi()),
    private val refreshGoalsUseCase: RefreshGoalsUseCase = RefreshGoalsUseCase(repository),
    private val createGoalUseCase: CreateGoalUseCase = CreateGoalUseCase(repository),
    private val transferToGoalUseCase: TransferToGoalUseCase = TransferToGoalUseCase(repository),
    private val goalsStateReducer: GoalsStateReducer = GoalsStateReducer(),
    private val dialogStateReducer: CreateGoalDialogStateReducer = CreateGoalDialogStateReducer(),
) : ViewModel() {
    private val clientState = MutableStateFlow(
        value = GoalsState(
            goals = emptyList(),
            isLoading = true
        )
    )

    private val dialogState = MutableStateFlow(
        value = CreateGoalDialogState(
            isShown = false,
            isLoading = false,
            error = null
        )
    )

    private val _effect = MutableSharedFlow<GoalsScreenEffects>()
    val effect = _effect.shareIn(viewModelScope, SharingStarted.Eagerly)

    val dialogUiState = dialogState.map(dialogStateReducer::computeUiState).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = CreateGoalDialogUiState.Hidden
    )

    val uiState =
        clientState.map { goalsStateReducer.computeUiState(it, roundUpToTransfer) }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = GoalsUiState.Loading
        )

    init {
        refreshGoals()
    }

    fun refreshGoals() {
        viewModelScope.launch(Dispatchers.IO) {
            refreshGoalsUseCase(
                accountUid = accountUid,
                clientState
            )
        }
    }

    fun showCreateGoalDialog(show: Boolean) {
        dialogState.update { it.copy(isShown = show) }
    }

    fun createGoal(
        name: String,
        currencyCode: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            createGoalUseCase(
                accountUid = accountUid,
                clientStateFlow = dialogState,
                params = CreateGoalParams(
                    name = name,
                    currencyCode = currencyCode
                ),
                onSuccess = {
                    refreshGoals()
                }
            )
        }
    }

    fun navigateToAccounts() {
        viewModelScope.launch {
            _effect.emit(GoalsScreenEffects.ReturnToAccounts)
        }
    }

    fun showToast(message: String) {
        viewModelScope.launch {
            _effect.emit(GoalsScreenEffects.ShowToast(message))
        }
    }

    fun transferTo(goal: Goal, roundUp: Amount) {
        viewModelScope.launch(Dispatchers.IO) {
            transferToGoalUseCase(
                accountUid = accountUid,
                clientStateFlow = clientState,
                goalUid = goal.uid,
                amount = roundUp,
                onSuccess = {
                    showToast("Successfully transferred $roundUp to ${goal.name}")
                    navigateToAccounts()
                }
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun create(
            owner: ViewModelStoreOwner,
            accountUid: String?,
            roundUpToTransfer: Amount?
        ) = ViewModelProvider(
            owner,
            factory(accountUid, roundUpToTransfer)
        )[GoalsScreenVm::class.java]

        private fun factory(
            accountUid: String?,
            roundUpToTransfer: Amount?
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                GoalsScreenVm(accountUid, roundUpToTransfer) as T
        }
    }
}
