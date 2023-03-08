package com.example.starlingtest.ui.navigation

import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.starlingtest.ui.roundups.RoundupsScreen
import com.example.starlingtest.ui.roundups.viewmodels.RoundupsScreenVm

fun NavGraphBuilder.transactionsDestination(
    navController: NavController
) {
    composable(
        route = "${Destination.TRANSACTIONS.name}/{accountUid}/{mainWalletUid}",
        arguments = listOf(
            navArgument("accountUid") {
                type = NavType.StringType
            },
            navArgument("mainWalletUid") {
                type = NavType.StringType
            }
        )
    ) {
        val vmStoreOwner = LocalViewModelStoreOwner.current!!
        val accountUid = it.arguments?.getString("accountUid")
        val mainWalletUid = it.arguments?.getString("mainWalletUid")
        val vm = RoundupsScreenVm.create(
            owner = vmStoreOwner,
            accountUid = accountUid,
            mainWalletUid = mainWalletUid,
            onRoundUp = { amount ->
                /* navController.navigate(
                     "${Destination.ROUND_UP.name}/$accountUid/$mainWalletUid/$amount"
                 )*/
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
