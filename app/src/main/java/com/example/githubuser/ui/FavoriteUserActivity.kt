package com.example.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.User
import com.example.githubuser.adapter.ListUserAdapter
import com.example.githubuser.data.local.entity.UsersEntity
import com.example.githubuser.databinding.ActivityFavoriteUserBinding
import com.example.githubuser.ui.model.UsersViewModel
import com.example.githubuser.ui.model.UsersViewModelFactory

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserBinding

    private val factory: UsersViewModelFactory by lazy { UsersViewModelFactory.getInstance(applicationContext) }
    private val viewModel: UsersViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getFavoriteUser().observe(this) { users ->
            setUserDataList(users)
            setEmptyIndicator(users.isEmpty())
        }

        val actionBar = supportActionBar
        actionBar?.title = getString(R.string.followed_user)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        binding.rvUser.layoutManager = LinearLayoutManager(this)

    }

    private fun setEmptyIndicator(isEmpty: Boolean) {
        binding.emptyIndicator.emptyMessage.text = getString(R.string.no_followed_user)
        if(isEmpty){
            binding.emptyIndicator.root.visibility = View.VISIBLE
        }else {
            binding.emptyIndicator.root.visibility = View.INVISIBLE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUserDataList(users: List<UsersEntity>) {
        val listUserAdapter = ListUserAdapter(roomToUser(users))
        binding.rvUser.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                goToDetail(data)
            }
        })
    }

    private fun goToDetail(data: User) {
        val sendObjectIntent = Intent(this@FavoriteUserActivity, UserDetailActivity::class.java)
        sendObjectIntent.putExtra(HomeActivity.EXTRA_USER, data.login)
        startActivity(sendObjectIntent)
    }

    private fun roomToUser(users: List<UsersEntity>): List<User> {
        return users.map { user ->
            User(
                login = user.username,
                name = user.name ?: "",
                avatarUrl = user.avatarUrl ?: "",
                location = user.location ?: "",
                followers = user.followers ?: 0,
                following = user.following ?: 0,
                publicRepos = user.repositories ?: 0)
        }
    }
}