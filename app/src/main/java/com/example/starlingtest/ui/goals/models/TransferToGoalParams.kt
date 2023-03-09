package com.example.starlingtest.ui.goals.models

import com.example.starlingtest.ui.roundups.models.AmountModel
import com.example.starlingtest.ui.roundups.states.Amount
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransferToGoalParams(
    @SerialName("amount")
    val amount: AmountModel,
)
