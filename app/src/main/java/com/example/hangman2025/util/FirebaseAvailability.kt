package com.example.hangman2025.util

import android.content.Context
import com.google.firebase.FirebaseApp

object FirebaseAvailability {
    @Volatile private var cached: Boolean? = null

    fun isConfigured(context: Context): Boolean {
        val current = cached
        if (current != null) return current
        val available = try {
            FirebaseApp.getApps(context).isNotEmpty()
        } catch (_: Exception) {
            false
        }
        cached = available
        return available
    }
}
