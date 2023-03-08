@file:OptIn(ExperimentalMaterialApi::class)

package com.example.starlingtest.ui.goals

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.starlingtest.ui.ErrorScreen
import com.example.starlingtest.ui.LoadingScreen
import com.example.starlingtest.ui.goals.states.CreateGoalDialogUiState
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
                val dialogUiState = viewModel.dialogUiState.collectAsState()
                CreateGoalDialog(
                    state = dialogUiState.value,
                    onConfirm = { name ->
                        viewModel.createGoal(name, uiState.currencyCode)
                    }
                )
                when (uiState) {
                    is GoalsUiState.Content.Goals -> GoalList(
                        uiState,
                        onClick = { goal ->
                            viewModel.onTap(goal)
                        }
                    )
                    GoalsUiState.Content.NoGoals -> ErrorScreen(
                        message = "No goals found",
                        onClick = { viewModel.showCreateGoalDialog() },
                        ctaText = "Create a goal"
                    )
                }
            }
        }
    }
}

@Composable
fun CreateGoalDialog(
    state: CreateGoalDialogUiState,
    onConfirm: (String) -> Unit
) {
    when (state) {
        CreateGoalDialogUiState.Hidden -> {
            // Do nothing
        }
        is CreateGoalDialogUiState.Shown -> Dialog(onDismissRequest = { }) {
            Surface(
                color = MaterialTheme.colors.background
            ) {
                val input = remember { mutableStateOf("") }
                val isError = remember { mutableStateOf(state.error != null) }

                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = input.value,
                        singleLine = true,
                        onValueChange = {
                            input.value = it
                            isError.value = false
                        },
                        label = {
                            Text(text = "Goal name")
                        },
                        isError = isError.value
                    )
                    if (isError.value) {
                        Text(
                            text = state.error!!,
                            color = MaterialTheme.colors.error,
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        Button(
                            enabled = input.value.isNotBlank() && state.isLoading.not(),
                            onClick = { onConfirm(input.value) }
                        ) {
                            if (state.isLoading) {
                                Text(text = "Creating...")
                            } else {
                                Text(text = "Create")
                            }
                        }
                    }
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
fun CreateGoalDialogPreview() {
    StarlingTestTheme {
        CreateGoalDialog(
            state = CreateGoalDialogUiState.Shown(
                error = "Error creating goal"
            ),
            onConfirm = {}
        )
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
                    ),
                    currencyCode = "GBP"
                ),
                onClick = {}
            )
        }
    }
}
