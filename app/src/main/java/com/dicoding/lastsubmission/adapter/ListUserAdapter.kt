package com.dicoding.lastsubmission.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.lastsubmission.R
import com.dicoding.lastsubmission.model.Items
import de.hdodenhof.circleimageview.CircleImageView

class ListUserAdapter: RecyclerView.Adapter<ListUserAdapter.ListUserHolder>() {

    private lateinit var onItemClickListener: OnClickListener
    private val items = ArrayList<Items>()

    fun setData(items: ArrayList<Items>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun setOnClickListener(onItemClickListener: OnClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListUserHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_list, parent, false)
        return ListUserHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ListUserHolder, position: Int) {
        val dataUser = items[position]

        Glide.with(holder.itemView.context)
            .load(dataUser.avatar_url)
            .into(holder.avatar)
        holder.username.text = dataUser.login
        holder.itemView.setOnClickListener { onItemClickListener.onItemCLicked(items[holder.adapterPosition]) }
    }

    inner class ListUserHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var avatar: CircleImageView = itemView.findViewById(R.id.avatar)
        var username: TextView = itemView.findViewById(R.id.username)
    }

    interface OnClickListener {
        fun onItemCLicked(items: Items)
    }
}