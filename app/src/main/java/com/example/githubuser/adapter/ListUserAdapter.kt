package com.example.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
<<<<<<< Updated upstream
import com.example.githubuser.data.local.entity.UsersEntity
import com.example.githubuser.databinding.ItemUserBinding

class ListUserAdapter(private val listUser: List<UsersEntity>) :
    RecyclerView.Adapter<ListUserAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvUserName: TextView = binding.userName
        val userPhoto: ImageView = binding.userPhoto
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
=======
import com.example.githubuser.User
import com.example.githubuser.databinding.ItemUserBinding

class ListUserAdapter(private val listUser: List<User>) : RecyclerView.Adapter<ListUserAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(val binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root) {
        val tvUserName: TextView = binding.userName
        val userPhoto: ImageView = binding.userPhoto
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
>>>>>>> Stashed changes
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val avatarUrl = listUser[position].avatarUrl
<<<<<<< Updated upstream
        val username: String = listUser[position].username
=======
        val username:String = listUser[position].login
>>>>>>> Stashed changes
        Glide.with(holder.userPhoto.context).load(avatarUrl).into(holder.userPhoto)
        holder.tvUserName.text = username.replaceFirstChar(Char::uppercase)

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = listUser.size

    interface OnItemClickCallback {
<<<<<<< Updated upstream
        fun onItemClicked(data: UsersEntity)
    }


=======
        fun onItemClicked(data: User)
    }

>>>>>>> Stashed changes
}