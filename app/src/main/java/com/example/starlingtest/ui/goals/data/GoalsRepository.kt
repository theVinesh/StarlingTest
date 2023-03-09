package com.example.starlingtest.ui.goals.data

import com.example.starlingtest.api.StarlingApi
import com.example.starlingtest.ui.goals.models.CreateGoalParams
import com.example.starlingtest.ui.goals.models.SavingsResponse
import com.example.starlingtest.ui.goals.models.TransferToGoalParams
import com.example.starlingtest.ui.roundups.models.AmountModel
import com.example.starlingtest.ui.roundups.states.Amount
import com.example.starlingtest.utils.generateUUID
import com.example.starlingtest.utils.networking.NetworkResponse

class GoalsRepository(private val api: StarlingApi) {
    suspend fun fetchGoals(
        accountUid: String
    ): NetworkResponse<SavingsResponse> = api.fetchGoals(accountUid)

    suspend fun createGoal(
        accountUid: String,
        params: CreateGoalParams
    ): NetworkResponse<Unit> = api.createGoal(accountUid, params)

    suspend fun transferToGoal(
        accountUid: String,
        goalUid: String,
        amount: Amount
    ): NetworkResponse<Unit> = api.transferToGoal(
        accountUid, goalUid, generateUUID(), TransferToGoalParams(
            amount = AmountModel(
                amount.currency,
                amount.amountInMinorUnits,
            )
        )
    )
}
