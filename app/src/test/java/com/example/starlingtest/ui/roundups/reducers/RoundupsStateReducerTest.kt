package com.example.starlingtest.ui.roundups.reducers

import com.example.starlingtest.ui.roundups.models.AmountModel
import com.example.starlingtest.ui.roundups.models.Direction
import com.example.starlingtest.ui.roundups.models.TransactionModel
import com.example.starlingtest.ui.roundups.models.TransactionStatus
import com.example.starlingtest.ui.roundups.states.Amount
import com.example.starlingtest.ui.roundups.states.RoundupsState
import com.example.starlingtest.ui.roundups.states.RoundupsUiState
import com.example.starlingtest.ui.roundups.states.Transaction
import io.kotest.matchers.shouldBe
import org.junit.Test
import java.time.LocalDate

internal val transactions = listOf(
    TransactionModel(
        uid = "uid1",
        direction = Direction.OUT,
        status = TransactionStatus.SETTLED,
        amount = AmountModel(inMinorUnits = 435, currency = "GBP")
    ),
    TransactionModel(
        uid = "uid2",
        direction = Direction.IN,
        status = TransactionStatus.PENDING,
        amount = AmountModel(inMinorUnits = 100, currency = "GBP")
    ),
    TransactionModel(
        uid = "uid3",
        direction = Direction.IN,
        status = TransactionStatus.SETTLED,
        amount = AmountModel(inMinorUnits = 520, currency = "GBP")
    ),
    TransactionModel(
        uid = "uid4",
        direction = Direction.OUT,
        status = TransactionStatus.SETTLED,
        amount = AmountModel(inMinorUnits = 520, currency = "GBP")
    ),
    TransactionModel(
        uid = "uid5",
        direction = Direction.OUT,
        status = TransactionStatus.SETTLED,
        amount = AmountModel(inMinorUnits = 87, currency = "GBP")
    )
)


internal class RoundupsStateReducerTest {
    private val reducer = RoundupsStateReducer()

    @Test
    fun `given a roundups state computeUIState should reduce to the correct ui state `() {
        // Initial clientState
        var clientState = RoundupsState()
        // Check if ui state is Initial
        reducer.computeUiState(clientState) shouldBe RoundupsUiState.Content.Initial

        // After error is set
        clientState = clientState.copy(error = "error")
        // Check if ui state is Error
        reducer.computeUiState(clientState) shouldBe RoundupsUiState.Error("error")

        // After date is set but without transactions
        val date = LocalDate.now()
        clientState = clientState.copy(since = LocalDate.now(), error = null)
        // Check if ui state is NoTransactions
        reducer.computeUiState(clientState) shouldBe RoundupsUiState.Content.NoTransactions

        // After loading is set
        clientState = clientState.copy(error = null, isLoading = true)
        // Check if ui state is Loading
        reducer.computeUiState(clientState) shouldBe RoundupsUiState.Loading

        // After date is set and with transactions
        clientState = clientState.copy(transactions = transactions, isLoading = false)
        // Check if ui state is Transactions
        val out = reducer.computeUiState(clientState)
        assert(out is RoundupsUiState.Content.Transactions)
        // Check if the computed roundup total is correct
        (out as RoundupsUiState.Content.Transactions).transactionsWithRoundUp.roundUpTotal shouldBe Amount(
            158,
            "GBP"
        )
    }

    @Test
    fun `given a transaction list getSettledOutboundTransactions should return only settled outbound transactions `() {

        // Act
        val output = reducer.getSettledOutboundTransactions(transactions)

        // Assert
        output shouldBe transactions.filter { it.direction == Direction.OUT && it.status == TransactionStatus.SETTLED }
            .map {
                Transaction(
                    it.uid,
                    it.counterPartyName,
                    Amount(it.amount.inMinorUnits, it.amount.currency)
                )
            }
    }
}
