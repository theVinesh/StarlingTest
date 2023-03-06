package com.example.starlingtest.ui.accounts.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccountsResponse(
    @SerialName("accounts")
    val accounts: List<AccountModel> = listOf(),
)

@Serializable
data class AccountModel(
    @SerialName("accountUid")
    val accountUid: String = "",
    @SerialName("defaultCategory")
    val defaultCategory: String = "",
    @SerialName("currency")
    val currency: String = "",
    @SerialName("name")
    val name: String = ""
)
