package com.example.starlingtest.ui.goals.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalsScreenVm @Inject constructor(
    private val refreshGoalsUseCase: RefreshGoalsUseCase,
    private val createGoalUseCase: CreateGoalUseCase,
    private val transferToGoalUseCase: TransferToGoalUseCase,
    private val goalsStateReducer: GoalsStateReducer,
    private val dialogStateReducer: CreateGoalDialogStateReducer,
) : ViewModel() {
    var accountUid: String? = null
    var roundUpToTransfer: Amount? = null

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
}
