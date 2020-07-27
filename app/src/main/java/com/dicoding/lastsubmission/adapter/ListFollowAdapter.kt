package com.dicoding.lastsubmission.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.lastsubmission.R
import com.dicoding.lastsubmission.model.ListFollow
import de.hdodenhof.circleimageview.CircleImageView

class ListFollowAdapter(private val follow: ArrayList<ListFollow>): RecyclerView.Adapter<ListFollowAdapter.ListFollowHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListFollowHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_follow, parent, false)
        return ListFollowHolder(view)
    }

    override fun getItemCount(): Int = follow.size

    override fun onBindViewHolder(holder: ListFollowAdapter.ListFollowHolder, position: Int) {
        val userFollow = follow[position]

        Glide.with(holder.itemView.context)
            .load(userFollow.avatar_url)
            .into(holder.avatarFollow)
        holder.usernameFollow.text = userFollow.login
    }

    inner class ListFollowHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var avatarFollow: CircleImageView = itemView.findViewById(R.id.avatar_follow)
        var usernameFollow: TextView = itemView.findViewById(R.id.username_follow)
    }
}