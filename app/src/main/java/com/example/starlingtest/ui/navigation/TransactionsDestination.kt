package com.example.starlingtest.ui.navigation

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
    navController: NavController,
    createVm: (String?, String?) -> RoundupsScreenVm
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
        val accountUid = it.arguments?.getString(KEY_ACCOUNT_ID)
        val mainWalletUid = it.arguments?.getString(KEY_WALLET_ID)
        val vm = createVm(accountUid, mainWalletUid)
        RoundupsScreen(
            viewModel = vm,
            onBack = {
                navController.navigateUp()
            },
            onRoundUp = { amount ->
                navController.navigate(
                    route = getGoalsRoute(
                        accountUid = accountUid!!,
                        amount = amount
                    )
                )
            }
        )
    }
}
