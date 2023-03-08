package com.example.starlingtest.ui.transactions.states

sealed class TransactionScreenEffects {
    data class ShowDatePicker(val show: Boolean) : TransactionScreenEffects()
}
