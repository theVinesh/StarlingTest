package com.example.starlingtest.ui.goals.usecases

import com.example.starlingtest.ui.goals.data.GoalsRepository
import com.example.starlingtest.ui.goals.states.GoalsState
import com.example.starlingtest.utils.networking.NetworkResponse
import com.example.starlingtest.utils.networking.getErrorMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class RefreshGoalsUseCase(
    private val goalsRepository: GoalsRepository
) {
    suspend operator fun invoke(
        accountUid: String?,
        clientStateFlow: MutableStateFlow<GoalsState>
    ) {
        val currentClientState = clientStateFlow.value
        if (accountUid == null) {
            clientStateFlow.update {
                currentClientState.copy(
                    goals = emptyList(),
                    error = "Invalid account id",
                    isLoading = false
                )
            }
            return
        }
        // Show loading
        clientStateFlow.update {
            currentClientState.copy(
                goals = emptyList(),
                isLoading = true
            )
        }
        // Fetch accounts
        when (val response = goalsRepository.fetchGoals(accountUid)) {
            is NetworkResponse.Success -> {
                clientStateFlow.update {
                    currentClientState.copy(
                        goals = response.body.goals,
                        isLoading = false,
                        error = null
                    )
                }
            }
            else -> {
                clientStateFlow.update {
                    currentClientState.copy(
                        goals = emptyList(),
                        isLoading = false,
                        error = response.getErrorMessage()
                    )
                }
            }
        }
    }
}
