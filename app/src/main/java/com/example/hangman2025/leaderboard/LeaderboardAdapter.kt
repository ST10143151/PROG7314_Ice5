package com.example.hangman2025.leaderboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hangman2025.databinding.ItemScoreBinding

class LeaderboardAdapter(private val data: List<Score>) : RecyclerView.Adapter<LeaderboardAdapter.VH>() {
    class VH(val b: ItemScoreBinding) : RecyclerView.ViewHolder(b.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(ItemScoreBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    override fun getItemCount(): Int = data.size
    override fun onBindViewHolder(holder: VH, position: Int) {
        val s = data[position]
        holder.b.tvUsername.text = s.username
        holder.b.tvScore.text = s.score.toString()
        holder.b.tvWord.text = s.word.uppercase()
    }
}
