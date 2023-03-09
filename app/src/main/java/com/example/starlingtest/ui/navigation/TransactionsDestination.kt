package com.example.starlingtest.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.starlingtest.ui.roundups.RoundupsScreen
import com.example.starlingtest.ui.roundups.viewmodels.RoundupsScreenVm

private const val KEY_ACCOUNT_ID = "roundups-accountUid"
private const val KEY_WALLET_ID = "roundups-mainWalletUid"
fun NavGraphBuilder.transactionsDestination(
    navController: NavController
) {
    composable(
        route = "${Destination.TRANSACTIONS.name}/{$KEY_ACCOUNT_ID}/{$KEY_WALLET_ID}",
        arguments = listOf(
            navArgument(KEY_ACCOUNT_ID) {
                type = NavType.StringType
            },
            navArgument(KEY_WALLET_ID) {
                type = NavType.StringType
            }
        )
    ) {
        val vm = hiltViewModel<RoundupsScreenVm>().apply {
            accountUid = it.arguments?.getString(KEY_ACCOUNT_ID)
            mainWalletUid = it.arguments?.getString(KEY_WALLET_ID)
        }
        RoundupsScreen(
            viewModel = vm,
            onBack = {
                navController.navigateUp()
            },
            onRoundUp = { amount ->
                navController.navigate(
                    route = getGoalsRoute(
                        accountUid = vm.accountUid!!,
                        amount = amount
                    )
                )
            }
        )
    }
}
