package com.example.starlingtest.ui.navigation

import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.starlingtest.ui.accounts.AccountsScreen
import com.example.starlingtest.ui.accounts.viewmodels.AccountsScreenVm

fun NavGraphBuilder.accountsDestination(
    navController: NavController,
    vmOwner: ViewModelStoreOwner
) {
    composable(
        route = Destination.ACCOUNTS.name
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
