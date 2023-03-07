package com.example.starlingtest.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.starlingtest.ui.theme.StarlingTestTheme

@Composable
fun ErrorScreen(
    message: String,
    onClick: () -> Unit = {},
    ctaText: String? = null
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message)
        if (ctaText != null) {
            Button(onClick = onClick) {
                Text(text = ctaText)
            }
        }
    }
}

@Preview
@Composable
fun ErrorScreenPreview() {
    StarlingTestTheme() {
        Surface() {
            ErrorScreen(message = "Something went wrong")

        }
    }
}

@Preview
@Composable
fun ErrorScreenWithCTAPreview() {
    StarlingTestTheme() {
        Surface() {
            ErrorScreen(
                message = "Something went wrong",
                ctaText = "Retry"
            )
        }
    }
}
