package com.example.starlingtest.ui.goals.usecases

import com.example.starlingtest.ui.goals.data.GoalsRepository
import com.example.starlingtest.ui.goals.models.TransferToGoalParams
import com.example.starlingtest.ui.goals.states.GoalsState
import com.example.starlingtest.ui.roundups.states.Amount
import com.example.starlingtest.utils.networking.NetworkResponse
import com.example.starlingtest.utils.networking.getErrorMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class TransferToGoalUseCase @Inject constructor(
    private val goalsRepository: GoalsRepository,
) {
    suspend operator fun invoke(
        accountUid: String?,
        goalUid: String?,
        amount: Amount,
        clientStateFlow: MutableStateFlow<GoalsState>,
        onSuccess: () -> Unit = {}
    ) {
        val currentClientState = clientStateFlow.value
        if (accountUid == null || goalUid == null) {
            clientStateFlow.update {
                currentClientState.copy(
                    error = "Invalid account id or goal id",
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
        when (val response = goalsRepository.transferToGoal(accountUid, goalUid, amount)) {
            is NetworkResponse.Success -> {
                clientStateFlow.update {
                    currentClientState.copy(
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
