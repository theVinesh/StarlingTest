package com.example.starlingtest

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.starlingtest.api.ApiFactory
import com.example.starlingtest.ui.theme.StarlingTestTheme
import com.example.starlingtest.utils.networking.NetworkResponse

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StarlingTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    LaunchedEffect(key1 = "") {
        when (val response = ApiFactory().createStarlingTestApi().fetchAccounts()) {
            is NetworkResponse.ClientError -> {
                Log.e("Vinesh", "Client error", response.error)
            }
            is NetworkResponse.NetworkError -> {
                Log.e("Vinesh", "Could not connect to internet", response.error)
            }
            is NetworkResponse.ServerError -> {
                Log.e("Vinesh", "Server error: ${response.code}")
            }
            is NetworkResponse.Success -> {
                Log.e("Vinesh", response.body.accounts.toString())
            }
        }
    }
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StarlingTestTheme {
        Greeting("Android")
    }
}
