package com.dicoding.lastsubmission.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.lastsubmission.R
import com.dicoding.lastsubmission.model.User
import de.hdodenhof.circleimageview.CircleImageView

class FavoriteAdapter: RecyclerView.Adapter<FavoriteAdapter.FavoriteHolder>() {

    private lateinit var onItemClickListener: OnItemClickListener

    var listFavorite = ArrayList<User>()
        set(listFavorite) {
            if (listFavorite.size > 0) {
                this.listFavorite.clear()
            }
            this.listFavorite.addAll(listFavorite)
            notifyDataSetChanged()
        }

    fun setOnClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_favorite, parent, false)
        return FavoriteHolder(view)
    }

    override fun getItemCount(): Int = this.listFavorite.size

    override fun onBindViewHolder(holder: FavoriteHolder, position: Int) {
        val userPosition = listFavorite[position]

        Glide.with(holder.itemView.context)
            .load(userPosition.avatar_url)
            .into(holder.avatarFav)
        holder.username.text = userPosition.login
        holder.fullname.text = userPosition.name
        holder.itemView.setOnClickListener { onItemClickListener.onItemClicked(listFavorite[holder.adapterPosition]) }
    }

    inner class FavoriteHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var avatarFav: CircleImageView = itemView.findViewById(R.id.avatar_fav)
        var username: TextView = itemView.findViewById(R.id.username_favorite)
        var fullname: TextView = itemView.findViewById(R.id.fullname_favorite)
    }

    interface OnItemClickListener {
        fun onItemClicked(user: User)
    }
}