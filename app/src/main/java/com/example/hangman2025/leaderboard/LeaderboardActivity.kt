package com.example.hangman2025.leaderboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hangman2025.data.FirestoreRepo
import com.example.hangman2025.databinding.ActivityLeaderboardBinding

class LeaderboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLeaderboardBinding
    private val repo = FirestoreRepo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvScores.layoutManager = LinearLayoutManager(this)
        repo.topScores { list -> binding.rvScores.adapter = LeaderboardAdapter(list) }
    }
}
