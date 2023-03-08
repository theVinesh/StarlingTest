package com.example.starlingtest.ui.goals.reducers

import com.example.starlingtest.ui.goals.states.CreateGoalDialogState
import com.example.starlingtest.ui.goals.states.CreateGoalDialogUiState


class CreateGoalDialogStateReducer {
    fun computeUiState(state: CreateGoalDialogState): CreateGoalDialogUiState = when {
        state.isShown -> CreateGoalDialogUiState.Shown(state.error, state.isLoading)
        else -> CreateGoalDialogUiState.Hidden
    }
}
