package com.example.hangman2025.leaderboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hangman2025.data.FirestoreRepo
import com.example.hangman2025.databinding.ActivityLeaderboardBinding
import com.example.hangman2025.util.FirebaseAvailability

class LeaderboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLeaderboardBinding
    private val repo by lazy { FirestoreRepo(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvScores.layoutManager = LinearLayoutManager(this)
        val hasFirebase = FirebaseAvailability.isConfigured(this)
        if (!hasFirebase) {
            binding.tvOffline.visibility = android.view.View.VISIBLE
            binding.rvScores.visibility = android.view.View.GONE
        } else {
            binding.tvOffline.visibility = android.view.View.GONE
            binding.rvScores.visibility = android.view.View.VISIBLE
            repo.topScores { list ->
                binding.rvScores.adapter = LeaderboardAdapter(list)
            }
        }
    }
}
