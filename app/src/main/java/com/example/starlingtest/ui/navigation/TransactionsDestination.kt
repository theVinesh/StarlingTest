package com.example.starlingtest.ui.navigation

import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.starlingtest.ui.accounts.AccountsScreen
import com.example.starlingtest.ui.accounts.viewmodels.AccountsScreenVm

fun NavGraphBuilder.transactionsDestination(
    navController: NavController,
    vmOwner: ViewModelStoreOwner
) {
    composable(
        route = "${Destination.TRANSACTIONS.name}/{accountUid}/{mainAccountUid}",
        arguments = listOf(
            navArgument("accountUid") {
                type = NavType.StringType
            },
            navArgument("mainAccountUid") {
                type = NavType.StringType
            }
        )
    ) {
        val vm = AccountsScreenVm.create(
            owner = vmOwner,
            onTap = {
                navController.navigate(
                    "${Destination.TRANSACTIONS.name}/${it.accountUid}/${it.mainWalletUid}"
                )
            }
        )
        AccountsScreen(
            viewModel = vm
        )
    }
}
