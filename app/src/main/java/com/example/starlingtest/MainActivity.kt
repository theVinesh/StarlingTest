package com.example.starlingtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.starlingtest.ui.navigation.Destination
import com.example.starlingtest.ui.navigation.accountsDestination
import com.example.starlingtest.ui.navigation.goalsDestination
import com.example.starlingtest.ui.navigation.transactionsDestination
import com.example.starlingtest.ui.theme.StarlingTestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

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
                            navController = navHostController
                        )
                        goalsDestination(
                            navController = navHostController
                        )
                    }
                }
            }
        }
    }
}
