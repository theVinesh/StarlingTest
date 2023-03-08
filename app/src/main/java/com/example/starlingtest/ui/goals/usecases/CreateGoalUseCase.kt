package com.example.starlingtest.ui.goals.usecases

import com.example.starlingtest.ui.goals.data.GoalsRepository
import com.example.starlingtest.ui.goals.models.CreateGoalParams
import com.example.starlingtest.ui.goals.states.CreateGoalDialogState
import com.example.starlingtest.utils.networking.NetworkResponse
import com.example.starlingtest.utils.networking.getErrorMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class CreateGoalUseCase(
    private val goalsRepository: GoalsRepository,
) {
    suspend operator fun invoke(
        accountUid: String?,
        params: CreateGoalParams,
        clientStateFlow: MutableStateFlow<CreateGoalDialogState>,
        onSuccess: () -> Unit = {}
    ) {
        val currentClientState = clientStateFlow.value
        if (accountUid == null) {
            clientStateFlow.update {
                currentClientState.copy(
                    error = "Invalid account id",
                    isLoading = false
                )
            }
            return
        }
        // Show loading
        clientStateFlow.update {
            currentClientState.copy(
                isLoading = true
            )
        }
        // Fire api call
        when (val response = goalsRepository.createGoal(accountUid, params)) {
            is NetworkResponse.Success -> {
                clientStateFlow.update {
                    currentClientState.copy(
                        isShown = false,
                        isLoading = false,
                        error = null
                    )
                }
                onSuccess()
            }
            else -> {
                clientStateFlow.update {
                    currentClientState.copy(
                        isLoading = false,
                        error = response.getErrorMessage()
                    )
                }
            }
        }
    }
}
