@file:OptIn(ExperimentalMaterialApi::class)

package com.example.starlingtest.ui.accounts

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
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.starlingtest.ui.ErrorScreen
import com.example.starlingtest.ui.LoadingScreen
import com.example.starlingtest.ui.accounts.states.Account
import com.example.starlingtest.ui.accounts.states.AccountsUIState
import com.example.starlingtest.ui.accounts.viewmodels.AccountsScreenVm
import com.example.starlingtest.ui.theme.StarlingTestTheme

@Composable
fun AccountsScreen(
    viewModel: AccountsScreenVm
) {
    val state = viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Accounts") }
            )
        }
    ) {
        it.calculateBottomPadding()
        when (val uiState = state.value) {
            is AccountsUIState.Loading -> LoadingScreen()
            is AccountsUIState.Error -> ErrorScreen(
                message = uiState.message,
                onClick = { viewModel.refreshAccounts() },
                ctaText = "Retry"
            )
            is AccountsUIState.Content -> {
                AccountsList(uiState)
            }
        }
    }
}

@Composable
fun AccountsList(
    uiState: AccountsUIState.Content
) {
    LazyColumn {
        items(
            uiState.accounts.size,
            key = { index -> uiState.accounts[index].accountUid }
        ) { index ->
            ListItem(
                overlineText = { Text(text = uiState.accounts[index].currency) },
                text = { Text(text = uiState.accounts[index].name) },
                trailing = {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "next"
                    )
                }
            )
            Divider()
        }
    }
}

@Preview
@Composable
fun AccountsListPreview() {
    StarlingTestTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            AccountsList(
                uiState = AccountsUIState.Content(
                    accounts = listOf(
                        Account(
                            accountUid = "1",
                            mainAccountUid = "",
                            currency = "GBP",
                            name = "Personal",
                        ),
                        Account(
                            accountUid = "2",
                            mainAccountUid = "",
                            currency = "GBP",
                            name = "Joint",
                        ),
                        Account(
                            accountUid = "3",
                            mainAccountUid = "",
                            currency = "EUR",
                            name = "Euro",
                        ),
                        Account(
                            accountUid = "4",
                            mainAccountUid = "",
                            currency = "INR",
                            name = "Indian Rupee",
                        )
                    )
                )
            )
        }
    }
}
