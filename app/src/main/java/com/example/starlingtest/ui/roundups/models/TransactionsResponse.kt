package com.example.starlingtest.ui.roundups.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionsResponse(
    @SerialName("feedItems")
    val items: List<TransactionModel> = listOf(),
)

@Serializable
data class TransactionModel(
    @SerialName("feedItemUid")
    val uid: String = "",
    @SerialName("amount")
    val amount: AmountModel = AmountModel(),
    @SerialName("direction")
    val direction: Direction = Direction.OUT,
    @SerialName("status")
    val status: TransactionStatus = TransactionStatus.SETTLED,
    @SerialName("counterPartyName")
    val counterPartyName: String = ""
)

@Serializable
data class AmountModel(
    @SerialName("currency")
    val currency: String = "",
    @SerialName("minorUnits")
    val inMinorUnits: Long = 0
)

@Serializable
enum class Direction {
    @SerialName("OUT")
    OUT,

    @SerialName("IN")
    IN
}

@Serializable
enum class TransactionStatus {
    @SerialName("SETTLED")
    SETTLED
}
