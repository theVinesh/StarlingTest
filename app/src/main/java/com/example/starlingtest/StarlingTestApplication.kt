package com.example.starlingtest

import android.app.Application
import android.widget.Toast
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class StarlingTestApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.ACCESS_TOKEN == "YOUR_ACCESS_TOKEN") {
            Toast.makeText(
                applicationContext,
                "Please add your access token to the gradle.properties file",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
