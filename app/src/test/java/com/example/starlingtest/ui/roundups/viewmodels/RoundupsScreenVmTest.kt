package com.example.starlingtest.ui.roundups.viewmodels

import com.example.starlingtest.ui.roundups.reducers.RoundupsStateReducer
import com.example.starlingtest.ui.roundups.states.RoundupsScreenEffects
import com.example.starlingtest.ui.roundups.states.RoundupsState
import com.example.starlingtest.ui.roundups.usecases.RefreshTransactionsUseCase
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import java.time.LocalDate


@OptIn(ExperimentalCoroutinesApi::class)
internal class RoundupsScreenVmTest {
    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    private val mockRefreshTransactionsUseCase: RefreshTransactionsUseCase = mockk(relaxed = true)
    private val mockStateReducer: RoundupsStateReducer = mockk(relaxed = true)
    private val vm by lazy {
        RoundupsScreenVm(
            refreshTransactionsUseCase = mockRefreshTransactionsUseCase,
            stateReducer = mockStateReducer
        )
    }

    init {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    @Before
    fun setUp() {
        vm.accountUid = "accountUid"
        vm.mainWalletUid = "mainWalletUid"
    }


    @Test
    fun `fetchTransactions() should call refreshTransactionsUseCase`() {
        // Arrange
        val since = LocalDate.now()

        // Act
        vm.fetchTransactions(since)

        // Assert
        coVerify { mockRefreshTransactionsUseCase(any(), any(), any(), since) }
    }

    @Test
    fun `showDatePicker() should emit ShowDatePicker effect`() = runBlockingTest {
        // Arrange
        val effects = mutableListOf<RoundupsScreenEffects>()
        val job = launch { vm.effect.toList(effects) }

        // Act
        vm.showDatePicker(true)

        // Assert
        effects shouldBe listOf(RoundupsScreenEffects.ShowDatePicker(true))
        job.cancel()
    }

    @Test
    fun `Test onDateSelected`() = runBlockingTest {
        // Arrange
        val date = LocalDate.now()
        val effects = mutableListOf<RoundupsScreenEffects>()
        val job = launch { vm.effect.toList(effects) }

        // Act
        vm.onDateSelected(date)

        // Assert
        // Client state should be updated resulting in a new UI state being computed
        coVerify {
            mockStateReducer.computeUiState(
                RoundupsState(
                    transactions = emptyList(),
                    since = date,
                    isLoading = false
                )
            )
        }
        // The date picker should be hidden
        effects shouldBe listOf(RoundupsScreenEffects.ShowDatePicker(false))

        // The transactions should be fetched for the selected date
        coVerify { mockRefreshTransactionsUseCase(any(), any(), any(), date) }

        job.cancel()
    }
}
