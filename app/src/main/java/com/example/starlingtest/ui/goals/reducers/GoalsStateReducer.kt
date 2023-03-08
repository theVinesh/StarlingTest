package com.example.starlingtest.ui.goals.reducers

import com.example.starlingtest.ui.goals.states.Goal
import com.example.starlingtest.ui.goals.states.GoalsState
import com.example.starlingtest.ui.goals.states.GoalsUiState
import com.example.starlingtest.ui.roundups.states.Amount


class GoalsStateReducer {
    fun computeUiState(state: GoalsState): GoalsUiState = when {
        state.error != null -> GoalsUiState.Error(state.error)
        state.isLoading -> GoalsUiState.Loading
        state.goals.isEmpty() -> GoalsUiState.Content.NoGoals("GBP") // TODO change this
        else -> GoalsUiState.Content.Goals(
            state.goals.map {
                Goal(
                    it.uid,
                    it.name,
                    Amount(it.savings.inMinorUnits, it.savings.currency)
                )
            },
            "GBP" // TODO change this
        )
    }
}
