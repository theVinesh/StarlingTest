package com.example.starlingtest.ui.goals.reducers

import com.example.starlingtest.ui.goals.states.CreateGoalDialogState
import com.example.starlingtest.ui.goals.states.CreateGoalDialogUiState
import javax.inject.Inject


class CreateGoalDialogStateReducer @Inject constructor() {
    fun computeUiState(state: CreateGoalDialogState): CreateGoalDialogUiState = when {
        state.isShown -> CreateGoalDialogUiState.Shown(state.error, state.isLoading)
        else -> CreateGoalDialogUiState.Hidden
    }
}
