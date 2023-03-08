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

    sealed interface Content : GoalsUiState {
        object NoGoals : Content
        data class Goals(val goals: List<Goal>) : Content
    }

    data class Error(val message: String) : GoalsUiState
}


data class Goal(
    val uid: String,
    val name: String,
    val savings: Amount
)
