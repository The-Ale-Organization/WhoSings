package com.musixmatch.whosings.ranking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.musixmatch.whosings.common.data.model.presentation.UserScoreItem
import com.musixmatch.whosings.databinding.RankingListItemBinding


class HighScoresAdapter constructor(private val items: List<UserScoreItem>) :
    RecyclerView.Adapter<HighScoresAdapter.UserScoreItemViewHolder>() {

    class UserScoreItemViewHolder(private val binding: RankingListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UserScoreItem) {
            binding.usernameTextView.text = item.userName
            binding.scoreTextView.text = item.score.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserScoreItemViewHolder {
        val itemBinding = RankingListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserScoreItemViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: UserScoreItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

}

