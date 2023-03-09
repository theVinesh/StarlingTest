package com.example.starlingtest.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
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
        val amount = it.arguments?.getLong(KEY_AMOUNT, 0L)
        val currency = it.arguments?.getString(KEY_CURRENCY)
        val vm = hiltViewModel<GoalsScreenVm>().apply {
            accountUid = it.arguments?.getString(KEY_ACCOUNT_ID)
            roundUpToTransfer = currency?.let { currencyCode -> Amount(amount!!, currencyCode) }
        }
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
