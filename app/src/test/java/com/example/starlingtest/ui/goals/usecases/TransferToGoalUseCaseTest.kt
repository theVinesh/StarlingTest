@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.starlingtest.ui.goals.usecases

import com.example.starlingtest.ui.goals.data.GoalsRepository
import com.example.starlingtest.ui.goals.states.GoalsState
import com.example.starlingtest.ui.roundups.states.Amount
import com.example.starlingtest.utils.networking.NetworkResponse
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

internal class TransferToGoalUseCaseTest {
    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    private val mockGoalRepository = mockk<GoalsRepository>(relaxed = true)
    private val useCase = TransferToGoalUseCase(mockGoalRepository)
    private val clientStateFlow = MutableStateFlow(GoalsState())

    init {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    @Before
    fun setUp() {
        clientStateFlow.value = GoalsState()

    }

    @Test
    fun `invoke() with accountUid null should send error`() = runBlockingTest {
        // Act
        useCase(
            accountUid = null,
            goalUid = "goalUid",
            amount = Amount(0, "GBP"),
            clientStateFlow = clientStateFlow
        )
        // Assert
        clientStateFlow.value shouldBe GoalsState(
            error = "Invalid account id or goal id",
            isLoading = false
        )
    }

    @Test
    fun `invoke() with goalUid null should send error`() = runBlockingTest {
        // Act
        useCase(
            accountUid = "accountUid",
            goalUid = null,
            amount = Amount(0, "GBP"),
            clientStateFlow = clientStateFlow
        )
        // Assert
        clientStateFlow.value shouldBe GoalsState(
            error = "Invalid account id or goal id",
            isLoading = false
        )
    }

    @Test
    fun `invoke() with valid params and success response should produce the correct state`() =
        runBlockingTest {
            coEvery {
                mockGoalRepository.transferToGoal(any(), any(), any())
            } returns NetworkResponse.Success(Unit)

            // Act
            useCase(
                accountUid = "accountUid",
                goalUid = "goalUid",
                amount = Amount(0, "GBP"),
                clientStateFlow = clientStateFlow
            )
            // Assert
            clientStateFlow.value shouldBe GoalsState(
                isLoading = false,
                error = null
            )
        }

    @Test
    fun `invoke() with valid params and failure response should produce the correct state`() =
        runBlockingTest {
            coEvery {
                mockGoalRepository.transferToGoal(any(), any(), any())
            } returns NetworkResponse.ServerError(500)

            // Act
            useCase(
                accountUid = "accountUid",
                goalUid = "goalUid",
                amount = Amount(0, "GBP"),
                clientStateFlow = clientStateFlow
            )
            // Assert
            clientStateFlow.value shouldBe GoalsState(
                isLoading = false,
                error = "Something went wrong on the server: 500"
            )
        }

}
