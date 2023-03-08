package com.example.starlingtest.ui.goals.data

import com.example.starlingtest.api.StarlingApi
import com.example.starlingtest.ui.goals.models.CreateGoalParams
import com.example.starlingtest.ui.goals.models.SavingsResponse
import com.example.starlingtest.utils.networking.NetworkResponse

class GoalsRepository(private val api: StarlingApi) {
    suspend fun fetchGoals(
        accountUid: String
    ): NetworkResponse<SavingsResponse> = api.fetchGoals(accountUid)

    suspend fun createGoal(
        accountUid: String,
        params: CreateGoalParams
    ): NetworkResponse<Unit> = api.createGoal(accountUid, params)
}
