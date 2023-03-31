package com.example.githubuser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ListUserAdapter(private val listUser: List<User>) : RecyclerView.Adapter<ListUserAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvUserName: TextView = view.findViewById(R.id.user_name)
        val UserPhoto: ImageView = view.findViewById(R.id.user_photo)
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_user, viewGroup, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val avatarUrl = listUser[position].avatarUrl
        val username:String = listUser[position].login
        Glide.with(holder.UserPhoto.context).load(avatarUrl).into(holder.UserPhoto)
        holder.tvUserName.text = username.replaceFirstChar(Char::uppercase)

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = listUser.size

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }

}