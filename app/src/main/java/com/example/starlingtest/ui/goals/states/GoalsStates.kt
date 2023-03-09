package com.example.starlingtest.ui.goals.states

import com.example.starlingtest.ui.goals.models.GoalModel
import com.example.starlingtest.ui.roundups.states.Amount

data class GoalsState(
    val goals: List<GoalModel> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false,
)

sealed interface GoalsUiState {
    object Loading : GoalsUiState

    sealed class Content(val roundUpToTransfer: Amount?) : GoalsUiState {
        class NoGoals(roundUpToTransfer: Amount?) : Content(roundUpToTransfer)
        class Goals(val goals: List<Goal>,  roundUpToTransfer: Amount?) :
            Content(roundUpToTransfer)
    }

    data class Error(val message: String) : GoalsUiState
}

data class CreateGoalDialogState(
    val isShown: Boolean = false,
    val error: String? = null,
    val isLoading: Boolean = false,
)

sealed interface CreateGoalDialogUiState {
    object Hidden : CreateGoalDialogUiState

    data class Shown(
        val error: String? = null,
        val isLoading: Boolean = false
    ) : CreateGoalDialogUiState
}

data class Goal(
    val uid: String,
    val name: String,
    val savings: Amount
)
