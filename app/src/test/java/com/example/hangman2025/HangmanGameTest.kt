package com.example.hangman2025

import com.example.hangman2025.game.GuessResult
import com.example.hangman2025.game.HangmanGame
import org.junit.Assert.*
import org.junit.Test

class HangmanGameTest {
    @Test fun correctGuessProgresses() {
        val g = HangmanGame(listOf("code"))
        val r = g.guessLetter('c')
        assertTrue(r == GuessResult.Correct || r == GuessResult.Win)
        assertTrue(g.displayWord().startsWith("C"))
    }

    @Test fun wrongGuessIncrements() {
        val g = HangmanGame(listOf("code"))
        val wrongBefore = g.wrongGuesses
        g.guessLetter('z')
        assertEquals(wrongBefore + 1, g.wrongGuesses)
    }

    @Test fun winScoresPositive() {
        val g = HangmanGame(listOf("a"))
        g.guessLetter('a')
        assertTrue(g.score() >= 100)
    }
}
