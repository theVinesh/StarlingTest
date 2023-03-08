package com.example.starlingtest.ui.navigation

import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
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
        val vmStoreOwner = LocalViewModelStoreOwner.current!!
        val accountUid = it.arguments?.getString(KEY_ACCOUNT_ID)
        val mainWalletUid = it.arguments?.getString(KEY_WALLET_ID)
        val vm = RoundupsScreenVm.create(
            owner = vmStoreOwner,
            accountUid = accountUid,
            mainWalletUid = mainWalletUid,
            onRoundUp = { amount ->
                navController.navigate(
                    "${Destination.GOALS.name}/$accountUid" // TODO send amount
                )
            }
        )
        RoundupsScreen(
            viewModel = vm,
            onBack = {
                navController.navigateUp()
            }
        )
    }
}
