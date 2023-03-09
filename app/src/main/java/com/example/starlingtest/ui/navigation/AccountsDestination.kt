package com.example.starlingtest.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.starlingtest.ui.accounts.AccountsScreen

fun NavGraphBuilder.accountsDestination(
    navController: NavController
) {
    composable(
        route = Destination.ACCOUNTS.name
    ) {
        AccountsScreen(
            viewModel = hiltViewModel(),
            onTapAccount = {
                navController.navigate(
                    "${Destination.TRANSACTIONS.name}/${it.accountUid}/${it.mainWalletUid}"
                )
            }
        )
    }
}
