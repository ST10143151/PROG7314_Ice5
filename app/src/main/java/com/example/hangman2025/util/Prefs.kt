package com.example.hangman2025.util

import android.content.Context

class Prefs(ctx: Context) {
    private val sp = ctx.getSharedPreferences("hangman_prefs", Context.MODE_PRIVATE)
    fun setUsername(u: String) { sp.edit().putString("username", u).apply() }
    fun getUsername(): String? = sp.getString("username", null)
}
