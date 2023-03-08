package com.example.starlingtest.ui.roundups.states

sealed class RoundupsScreenEffects {
    data class ShowDatePicker(val show: Boolean) : RoundupsScreenEffects()
}
