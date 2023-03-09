package com.example.starlingtest.ui.goals.reducers

import com.example.starlingtest.ui.goals.states.Goal
import com.example.starlingtest.ui.goals.states.GoalsState
import com.example.starlingtest.ui.goals.states.GoalsUiState
import com.example.starlingtest.ui.roundups.states.Amount
import javax.inject.Inject


class GoalsStateReducer @Inject constructor() {
    fun computeUiState(state: GoalsState, roundUpToTransfer: Amount?): GoalsUiState = when {
        state.error != null -> GoalsUiState.Error(state.error)
        state.isLoading -> GoalsUiState.Loading
        state.goals.isEmpty() -> GoalsUiState.Content.NoGoals(roundUpToTransfer)
        else -> GoalsUiState.Content.Goals(
            state.goals.map {
                Goal(
                    it.uid,
                    it.name,
                    Amount(it.savings.inMinorUnits, it.savings.currency)
                )
            },
            roundUpToTransfer
        )
    }
}
