package com.example.hangman2025.game

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.hangman2025.R

class HangmanView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val gallowsPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.purple_700)
        strokeWidth = 10f
        style = Paint.Style.STROKE
    }
    private val figurePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.teal_700)
        strokeWidth = 10f
        style = Paint.Style.STROKE
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, android.R.color.black)
        textAlign = Paint.Align.CENTER
    }
    private val hintPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, android.R.color.darker_gray)
        textAlign = Paint.Align.CENTER
        textSize = 32f
    }

    private var game: HangmanGame? = null

    fun bind(g: HangmanGame) { game = g; invalidate() }
    fun updateAndInvalidate() { invalidate() }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()
        // Draw gallows
        val baseY = h * 0.9f
        canvas.drawLine(w*0.15f, baseY, w*0.85f, baseY, gallowsPaint)
        canvas.drawLine(w*0.25f, baseY, w*0.25f, h*0.2f, gallowsPaint)
        canvas.drawLine(w*0.25f, h*0.2f, w*0.6f, h*0.2f, gallowsPaint)
        canvas.drawLine(w*0.6f, h*0.2f, w*0.6f, h*0.3f, gallowsPaint)

        val wrong = game?.wrongGuesses ?: 0
        // Head
        if (wrong >= 1) canvas.drawCircle(w*0.6f, h*0.38f, w*0.08f, figurePaint)
        // Body
        if (wrong >= 2) canvas.drawLine(w*0.6f, h*0.46f, w*0.6f, h*0.65f, figurePaint)
        // Left arm
        if (wrong >= 3) canvas.drawLine(w*0.6f, h*0.52f, w*0.52f, h*0.58f, figurePaint)
        // Right arm
        if (wrong >= 4) canvas.drawLine(w*0.6f, h*0.52f, w*0.68f, h*0.58f, figurePaint)
        // Left leg
        if (wrong >= 5) canvas.drawLine(w*0.6f, h*0.65f, w*0.54f, h*0.78f, figurePaint)
        // Right leg
        if (wrong >= 6) canvas.drawLine(w*0.6f, h*0.65f, w*0.66f, h*0.78f, figurePaint)

        // Draw masked word and guessed letters overlay for visualization
        val masked = game?.displayWord() ?: "_ _ _"
        textPaint.textSize = h * 0.06f
        canvas.drawText(masked, w * 0.5f, h * 0.12f, textPaint)

        val guessedLetters = game?.guessed?.joinToString(", ")?.uppercase() ?: ""
        canvas.drawText(guessedLetters, w * 0.5f, h * 0.16f, hintPaint)
    }
}
