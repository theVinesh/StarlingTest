package com.example.starlingtest.ui.accounts.data

import com.example.starlingtest.api.StarlingApi

class AccountsRepository(private val api:StarlingApi) {
    suspend fun fetchAccounts() = api.fetchAccounts()
}
