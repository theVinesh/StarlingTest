package com.example.starlingtest.ui.navigation

import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.starlingtest.ui.goals.GoalsScreen
import com.example.starlingtest.ui.goals.viewmodels.GoalsScreenVm
import com.example.starlingtest.ui.roundups.states.Amount

private const val KEY_ACCOUNT_ID = "goals-accountUid"
private const val KEY_AMOUNT = "goals-amount"
private const val KEY_CURRENCY = "goals-currency"

fun getGoalsRoute(accountUid: String, amount: Amount?) =
    "${Destination.GOALS.name}/$accountUid?$KEY_AMOUNT=${amount?.amountInMinorUnits}?$KEY_CURRENCY=${amount?.currency}"

fun NavGraphBuilder.goalsDestination(
    navController: NavController
) {
    composable(
        route = "${Destination.GOALS.name}/{$KEY_ACCOUNT_ID}?$KEY_AMOUNT={$KEY_AMOUNT}?{$KEY_CURRENCY}={$KEY_CURRENCY}",
        arguments = listOf(
            navArgument(KEY_ACCOUNT_ID) {
                type = NavType.StringType
            },
            navArgument(KEY_AMOUNT) {
                type = NavType.LongType
                defaultValue = 0L
            },
            navArgument(KEY_CURRENCY) {
                type = NavType.StringType
                nullable = true
            }
        )
    ) {
        val vmStoreOwner = LocalViewModelStoreOwner.current!!
        val amount = it.arguments?.getLong(KEY_AMOUNT, 0L)
        val currency = it.arguments?.getString(KEY_CURRENCY)
        val roundUpToTransfer = currency?.let { currencyCode -> Amount(amount!!, currencyCode) }
        val accountUid = it.arguments?.getString(KEY_ACCOUNT_ID)
        val vm = GoalsScreenVm.create(
            owner = vmStoreOwner,
            accountUid = accountUid,
            roundUpToTransfer = roundUpToTransfer
        )
        GoalsScreen(
            viewModel = vm,
            onBack = {
                navController.navigateUp()
            },
            onReturnToAccounts = {
                navController.popBackStack(Destination.ACCOUNTS.name, inclusive = false)
            }
        )
    }
}
