package com.example.starlingtest.ui.goals.models

import com.example.starlingtest.ui.roundups.models.AmountModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SavingsResponse(
    @SerialName("savingsGoalList")
    val goals: List<GoalModel> = listOf(),
)

@Serializable
data class GoalModel(
    @SerialName("savingsGoalUid")
    val uid: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("totalSaved")
    val savings: AmountModel = AmountModel(),
)
