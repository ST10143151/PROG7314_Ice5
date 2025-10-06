package com.example.hangman2025

import android.app.Application
import com.google.firebase.FirebaseApp
import com.example.hangman2025.util.FirebaseAvailability

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            FirebaseApp.initializeApp(this)
        } catch (_: Exception) {
        }
        FirebaseAvailability.isConfigured(this)
    }
}
