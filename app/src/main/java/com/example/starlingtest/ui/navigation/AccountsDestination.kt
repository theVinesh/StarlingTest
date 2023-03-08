package com.example.starlingtest.ui.navigation

import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.starlingtest.ui.accounts.AccountsScreen
import com.example.starlingtest.ui.accounts.viewmodels.AccountsScreenVm

fun NavGraphBuilder.accountsDestination(
    navController: NavController
) {
    composable(
        route = Destination.ACCOUNTS.name
    ) {
        val vmStoreOwner = LocalViewModelStoreOwner.current!!
        val vm = AccountsScreenVm.create(
            owner = vmStoreOwner,
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
