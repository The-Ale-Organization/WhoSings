package com.musixmatch.whosings.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.musixmatch.whosings.R
import com.musixmatch.whosings.data.model.UserScoreItem


class HighScoresAdapter constructor(private val items: List<UserScoreItem>) :
    RecyclerView.Adapter<HighScoresAdapter.UserScoreItemViewHolder>() {

    class UserScoreItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewAvatar = itemView.findViewById<ImageView>(R.id.imageViewAvatar)
        val userNameTextView = itemView.findViewById<TextView>(R.id.usernameTextView)
        val scoreTextView = itemView.findViewById<TextView>(R.id.scoreTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserScoreItemViewHolder {
        return UserScoreItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.ranking_list_item, parent, false)
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: UserScoreItemViewHolder, position: Int) {
        items[position].let {
            holder.userNameTextView.text = it.userName
            holder.scoreTextView.text = it.score.toString()
        }
    }


}

