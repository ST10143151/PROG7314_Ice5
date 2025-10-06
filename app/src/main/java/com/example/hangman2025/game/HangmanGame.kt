package com.example.hangman2025.game

import kotlin.random.Random

class HangmanGame(
    private val words: List<String> = defaultWords
) {
    companion object {
        val defaultWords = listOf(
            "android","kotlin","studio","canvas","layout","recycler","material","firebase","firestore","activity","fragment","intent","adapter","binding","viewmodel","lifecycle","navigation","repository","service","broadcast","notification","storage","bitmap","vector","gesture","keyboard","internet","socket","thread","handler","choreographer","dagger","hilt","retrofit","okhttp","testing","espresso","junit","gradle","manifest","package","resource","string","colors","styles","theme","vector","animation","transition","design","pattern","singleton","factory","observer","builder","iterator","strategy","command","memento","state","visitor","adapterp","decorator","proxy","bridge","composite","facade","flyweight","mvc","mvp","mvvm","clean","domain","data","presentation","mapper","entity","dto","json","xml","proto","parcel","serial","cipher","crypto","hash","secure","cookie","session","token","auth","login","logout","profile","leaderboard","score","hangman"
        )
    }

    private val random = Random(System.currentTimeMillis())

    lateinit var secretWord: String
        private set

    val guessed: MutableSet<Char> = linkedSetOf()
    var wrongGuesses: Int = 0
        private set
    val maxWrong: Int = 6

    val remainingAttempts: Int get() = maxWrong - wrongGuesses
    val isWin: Boolean get() = ::secretWord.isInitialized && secretWord.all { guessed.contains(it) }
    val isLose: Boolean get() = wrongGuesses >= maxWrong

    init { newRound() }

    fun newRound() {
        secretWord = words[random.nextInt(words.size)].lowercase()
        guessed.clear()
        wrongGuesses = 0
    }

    fun guessLetter(ch: Char): GuessResult {
        val c = ch.lowercaseChar()
        if (!c.isLetter()) return GuessResult.Ignored
        if (guessed.contains(c)) return GuessResult.Already
        guessed.add(c)
        return if (secretWord.contains(c)) {
            if (isWin) GuessResult.Win else GuessResult.Correct
        } else {
            wrongGuesses++
            if (isLose) GuessResult.Lose else GuessResult.Wrong
        }
    }

    fun displayWord(): String = secretWord.map { if (guessed.contains(it)) it.uppercaseChar() else '_' }.joinToString(" ")

    fun score(): Int = if (isWin) 100 + 10 * remainingAttempts else 0
}

enum class GuessResult { Correct, Wrong, Already, Ignored, Win, Lose }
