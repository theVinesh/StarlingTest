package com.example.starlingtest.ui.accounts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.starlingtest.ui.accounts.states.AccountsUIState
import com.example.starlingtest.ui.accounts.viewmodels.AccountsScreenVm

@Composable
fun AccountsScreen(
    viewModel: AccountsScreenVm
) {
    val state = viewModel.uiState.collectAsState()

    when (val uiState = state.value) {
        is AccountsUIState.Loading -> {
            CircularProgressIndicator()
        }
        is AccountsUIState.Error -> {
            Column() {
                Text(text = uiState.message)
                Button(onClick = {
                    viewModel.refreshAccounts()
                }) {
                    Text(text = "Retry")
                }
            }
        }
        is AccountsUIState.Content -> {
            LazyColumn {
                items(
                    uiState.accounts.size,
                    key = { index -> uiState.accounts[index].accountUid }
                ) { index ->
                    Text(text = uiState.accounts[index].name)
                }
            }
        }
    }

}
