package com.example.hangman2025

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.hangman2025.auth.AuthActivity
import com.example.hangman2025.data.FirestoreRepo
import com.example.hangman2025.databinding.ActivityMainBinding
import com.example.hangman2025.game.GuessResult
import com.example.hangman2025.game.HangmanGame
import com.example.hangman2025.leaderboard.LeaderboardActivity
import com.example.hangman2025.util.Prefs
import com.example.hangman2025.util.FirebaseAvailability
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var game: HangmanGame
    private val repo by lazy { FirestoreRepo(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val hasFirebase = FirebaseAvailability.isConfigured(this)
        if (hasFirebase) {
            if (Firebase.auth.currentUser == null) {
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
                return
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        game = HangmanGame()
        binding.hangmanView.bind(game)
        initKeyboard()
        refreshWord()

        binding.btnLeaderboard.setOnClickListener {
            startActivity(Intent(this, LeaderboardActivity::class.java))
        }

        binding.btnSetName.setOnClickListener {
            val input = EditText(this)
            input.setText(Prefs(this).getUsername() ?: "Player")
            AlertDialog.Builder(this)
                .setTitle("Set Username")
                .setView(input)
                .setPositiveButton("Save") { _, _ ->
                    val name = input.text.toString().trim().ifBlank { "Player" }
                    Prefs(this).setUsername(name)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun initKeyboard() {
        val grid: ViewGroup = binding.keyboardGrid
        val letters = ('A'..'Z').toList()
        letters.forEach { ch ->
            val btn = Button(this).apply {
                text = ch.toString()
                setOnClickListener {
                    isEnabled = false
                    handleGuess(ch)
                }
            }
            grid.addView(btn)
        }
    }

    private fun resetKeyboard() {
        for (i in 0 until binding.keyboardGrid.childCount) {
            (binding.keyboardGrid.getChildAt(i) as? Button)?.isEnabled = true
        }
    }

    private fun handleGuess(ch: Char) {
        when (val result = game.guessLetter(ch)) {
            GuessResult.Correct, GuessResult.Wrong, GuessResult.Already, GuessResult.Ignored -> {
                refreshWord()
                binding.hangmanView.updateAndInvalidate()
                if (game.isWin) showEndDialog(true)
                else if (game.isLose) showEndDialog(false)
            }
            GuessResult.Win -> { refreshWord(); showEndDialog(true) }
            GuessResult.Lose -> { refreshWord(); showEndDialog(false) }
        }
    }

    private fun refreshWord() {
        binding.tvMaskedWord.text = game.displayWord()
    }

    private fun showEndDialog(win: Boolean) {
        val score = game.score()
        val username = Prefs(this).getUsername() ?: "Player"
        repo.saveScore(username, score, game.secretWord) { }
        AlertDialog.Builder(this)
            .setTitle(if (win) "You Win!" else "You Lose")
            .setMessage("Word: ${game.secretWord}\nScore: $score")
            .setCancelable(false)
            .setPositiveButton("Play Again") { _, _ ->
                game.newRound()
                resetKeyboard()
                refreshWord()
                binding.hangmanView.updateAndInvalidate()
            }
            .setNegativeButton("Exit") { _, _ -> finish() }
            .show()
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            val c = event.unicodeChar.toChar()
            if (c.isLetter()) {
                for (i in 0 until binding.keyboardGrid.childCount) {
                    val b = binding.keyboardGrid.getChildAt(i) as? Button ?: continue
                    if (b.text.first().equals(c, ignoreCase = true)) {
                        if (b.isEnabled) { b.performClick(); break }
                    }
                }
                return true
            }
        }
        return super.dispatchKeyEvent(event)
    }
}
