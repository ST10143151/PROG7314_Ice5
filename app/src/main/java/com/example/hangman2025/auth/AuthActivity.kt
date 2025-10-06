package com.example.hangman2025.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hangman2025.MainActivity
import com.example.hangman2025.data.FirestoreRepo
import com.example.hangman2025.databinding.ActivityAuthBinding
import com.example.hangman2025.util.Prefs
import com.example.hangman2025.util.FirebaseAvailability
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private val auth by lazy { Firebase.auth }
    private val repo by lazy { FirestoreRepo(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!FirebaseAvailability.isConfigured(this)) {
            if (Prefs(this).getUsername().isNullOrBlank()) {
                Prefs(this).setUsername("Player")
            }
            Toast.makeText(this, "Firebase not configured. Running in offline mode.", Toast.LENGTH_SHORT).show()
            goMain(); return
        }

        auth.currentUser?.let { goMain(); return }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    repo.fetchUsername { name ->
                        Prefs(this).setUsername(name ?: email.substringBefore('@'))
                        goMain()
                    }
                }
                .addOnFailureListener { e -> Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show() }
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()
            val username = binding.etUsername.text.toString().ifBlank { email.substringBefore('@') }
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    repo.saveUserProfile(username, email) {
                        Prefs(this).setUsername(username)
                        goMain()
                    }
                }
                .addOnFailureListener { e -> Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show() }
        }
    }

    private fun goMain() { startActivity(Intent(this, MainActivity::class.java)); finish() }
}
