@file:OptIn(ExperimentalMaterialApi::class)

package com.example.starlingtest.ui.goals

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.starlingtest.ui.ErrorScreen
import com.example.starlingtest.ui.LoadingScreen
import com.example.starlingtest.ui.goals.states.Goal
import com.example.starlingtest.ui.goals.states.GoalsUiState
import com.example.starlingtest.ui.goals.viewmodels.GoalsScreenVm
import com.example.starlingtest.ui.roundups.states.Amount
import com.example.starlingtest.ui.theme.StarlingTestTheme

@Composable
fun GoalsScreen(
    viewModel: GoalsScreenVm,
    onBack: () -> Unit
) {
    val state = viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.clickable {
                            onBack()
                        }
                    )
                },
                title = { Text(text = "Goals") }
            )
        }
    ) {
        it.calculateBottomPadding()
        when (val uiState = state.value) {
            is GoalsUiState.Loading -> LoadingScreen()
            is GoalsUiState.Error -> ErrorScreen(
                message = uiState.message,
                onClick = { viewModel.refreshGoals() },
                ctaText = "Retry"
            )
            is GoalsUiState.Content -> {
                when (uiState) {
                    is GoalsUiState.Content.Goals -> GoalList(
                        uiState,
                        onClick = { goal ->
                            viewModel.onTap(goal)
                        }
                    )
                    GoalsUiState.Content.NoGoals -> ErrorScreen(
                        message = "No goals found",
                        onClick = { /*TODO*/ },
                        ctaText = "Create a goal"
                    )
                }
            }
        }
    }
}

@Composable
fun GoalList(
    uiState: GoalsUiState.Content.Goals,
    onClick: (Goal) -> Unit
) {
    LazyColumn {
        items(
            uiState.goals.size,
            key = { index -> uiState.goals[index].uid }
        ) { index ->
            val goal = uiState.goals[index]
            ListItem(
                text = { Text(text = goal.name) },
                trailing = {
                    Text(text = "${goal.savings.amountString} ${goal.savings.currency}")
                },
                modifier = Modifier.clickable {
                    onClick(goal)
                }
            )
            Divider()
        }
    }
}

@Preview
@Composable
fun GoalListPreview() {
    StarlingTestTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            GoalList(
                uiState = GoalsUiState.Content.Goals(
                    goals = listOf(
                        Goal(
                            uid = "1",
                            name = "Adventure",
                            savings = Amount(1000000, "GBP")
                        ),
                        Goal(
                            uid = "2",
                            name = "Savings",
                            savings = Amount(5000000, "GBP")
                        ),
                        Goal(
                            uid = "3",
                            name = "Fees",
                            savings = Amount(87000, "GBP")
                        )
                    )
                ),
                onClick = {}
            )
        }
    }
}
