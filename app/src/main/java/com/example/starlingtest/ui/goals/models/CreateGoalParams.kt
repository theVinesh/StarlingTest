package com.example.starlingtest.ui.goals.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateGoalParams(
    @SerialName("name")
    val name: String,
    @SerialName("currency")
    val currencyCode: String,
)
