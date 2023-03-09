package com.example.starlingtest.ui.goals.states

sealed interface GoalsScreenEffects {
    data class ShowToast(val message: String) : GoalsScreenEffects
    object ReturnToAccounts : GoalsScreenEffects
}
