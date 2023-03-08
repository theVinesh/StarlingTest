package com.example.starlingtest.ui.navigation

import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.starlingtest.ui.goals.GoalsScreen
import com.example.starlingtest.ui.goals.viewmodels.GoalsScreenVm

private const val KEY_ACCOUNT_ID = "goals-accountUid"

fun NavGraphBuilder.goalsDestination(
    navController: NavController
) {
    composable(
        route = "${Destination.GOALS.name}/{$KEY_ACCOUNT_ID}",
        arguments = listOf(
            navArgument(KEY_ACCOUNT_ID) {
                type = NavType.StringType
            }
        )
    ) {
        val vmStoreOwner = LocalViewModelStoreOwner.current!!
        val accountUid = it.arguments?.getString(KEY_ACCOUNT_ID)
        val vm = GoalsScreenVm.create(
            owner = vmStoreOwner,
            accountUid = accountUid,
            onTap = { goal ->
                /* navController.navigate(
                     "${Destination.ROUND_UP.name}/$accountUid/$mainWalletUid/$amount"
                 )*/
            }
        )
        GoalsScreen(
            viewModel = vm,
            onBack = {
                navController.navigateUp()
            }
        )
    }
}
