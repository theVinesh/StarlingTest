package com.example.starlingtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.starlingtest.ui.goals.viewmodels.GoalsScreenVm
import com.example.starlingtest.ui.navigation.Destination
import com.example.starlingtest.ui.navigation.accountsDestination
import com.example.starlingtest.ui.navigation.goalsDestination
import com.example.starlingtest.ui.navigation.transactionsDestination
import com.example.starlingtest.ui.roundups.viewmodels.RoundupsScreenVm
import com.example.starlingtest.ui.theme.StarlingTestTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var goalsScreenVmAssistedFactory: GoalsScreenVm.GoalsScreenVmAssistedFactory

    @Inject
    lateinit var roundUpsScreenVmAssistedFactory: RoundupsScreenVm.RoundupsScreenVmAssistedFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navHostController = rememberNavController()
            StarlingTestTheme {
                Box(
                    modifier = Modifier.navigationBarsPadding()
                ) {
                    NavHost(
                        navController = navHostController,
                        startDestination = Destination.ACCOUNTS.name
                    ) {
                        accountsDestination(
                            navController = navHostController
                        )
                        transactionsDestination(
                            navController = navHostController,
                            createVm = { accountUid, walletUid ->
                                val vm: RoundupsScreenVm by viewModels {
                                    RoundupsScreenVm.factory(
                                        roundUpsScreenVmAssistedFactory,
                                        accountUid,
                                        walletUid
                                    )
                                }
                                vm
                            }
                        )
                        goalsDestination(
                            navController = navHostController,
                            createVm = { accountUid, amount ->
                                val vm: GoalsScreenVm by viewModels {
                                    GoalsScreenVm.factory(
                                        goalsScreenVmAssistedFactory,
                                        accountUid,
                                        amount
                                    )
                                }
                                vm
                            }
                        )
                    }
                }
            }
        }
    }
}
