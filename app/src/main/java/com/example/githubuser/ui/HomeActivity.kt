package com.example.githubuser.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.ListUserAdapter
import com.example.githubuser.data.local.entity.UsersEntity
import com.example.githubuser.databinding.ActivityHomeBinding
import com.example.githubuser.viewmodel.UsersViewModel
import com.example.githubuser.viewmodel.ViewModelFactory

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var myObserver: Observer<com.example.githubuser.data.Result<List<UsersEntity>>>
    private val factory: ViewModelFactory by lazy { ViewModelFactory.getInstance(applicationContext) }
    private val viewModel: UsersViewModel by viewModels {
        factory
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        var QUERY = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myObserver = Observer { result ->
            when (result) {
                is com.example.githubuser.data.Result.Loading -> showLoading(true)
                is com.example.githubuser.data.Result.Success -> {
                    showLoading(false)
                    setUserDataList(result.data)
                }
                is com.example.githubuser.data.Result.Error -> {
                    showLoading(false)
                    showToast(result.error)
                }
            }
        }
        viewModel.getGithubUsers().observeForever(myObserver)
        binding.rvUser.layoutManager = LinearLayoutManager(this)
    }

    private fun setUserDataList(users: List<UsersEntity>) {
        val listUserAdapter = ListUserAdapter(users)
        binding.rvUser.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UsersEntity) {
                toDetail(data)
            }
        })
    }

    private fun toDetail(user: UsersEntity) {
        val sendObjectIntent = Intent(this@HomeActivity, UserDetailActivity::class.java)
        sendObjectIntent.putExtra(EXTRA_USER, user.username)
        startActivity(sendObjectIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val mMenuInflater = menuInflater
        mMenuInflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                QUERY = query
                viewModel.searchUser(QUERY).observeForever { result ->
                    when (result) {
                        is com.example.githubuser.data.Result.Error -> noInternetConnection(true)
                        else -> noInternetConnection(false)
                    }
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.setting -> true
            else -> false
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@HomeActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.rvUser.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.rvUser.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun noInternetConnection(isOffline: Boolean) {
        if (isOffline) {
            binding.rvUser.visibility = View.INVISIBLE
            binding.noInternetIndicator.root.visibility = View.VISIBLE
        } else {
            binding.rvUser.visibility = View.VISIBLE
            binding.noInternetIndicator.root.visibility = View.INVISIBLE
        }

    }

    override fun onResume() {
        super.onResume()
        if (!viewModel.getGithubUsers().hasObservers()) viewModel.getGithubUsers()
            .observe(this, myObserver)
    }

    override fun onPause() {
        super.onPause()
        viewModel.getGithubUsers().removeObservers(this)
        viewModel.searchUser(QUERY).removeObservers(this)
    }
}