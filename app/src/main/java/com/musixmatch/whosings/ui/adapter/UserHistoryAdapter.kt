package com.musixmatch.whosings.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.musixmatch.whosings.data.model.RecentGameItem
import com.musixmatch.whosings.databinding.RecentGameItemBinding


class UserHistoryAdapter constructor(private val items: List<RecentGameItem>) :
    RecyclerView.Adapter<UserHistoryAdapter.UserScoreItemViewHolder>() {

    class UserScoreItemViewHolder(private val binding: RecentGameItemBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: RecentGameItem) {
            binding.dateTextView.text = "${item.day}/${item.month}/${item.year}"
            binding.scoreTextView.text = item.score.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserScoreItemViewHolder {
        val itemBinding = RecentGameItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserScoreItemViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: UserScoreItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

}

