package com.example.starlingtest.ui.navigation

import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.starlingtest.ui.transactions.TransactionsScreen
import com.example.starlingtest.ui.transactions.viewmodels.TransactionsScreenVm

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
        val vm = TransactionsScreenVm.create(
            owner = vmStoreOwner,
            accountUid = accountUid,
            mainWalletUid = mainWalletUid,
            onRoundUp = { amount ->
                /* navController.navigate(
                     "${Destination.ROUND_UP.name}/$accountUid/$mainWalletUid/$amount"
                 )*/
            }
        )
        TransactionsScreen(
            viewModel = vm,
            onBack = {
                navController.navigateUp()
            }
        )
    }
}
