package com.example.githubuser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setInternetIndicator(isInternetConected(this))
        mainViewModel.getGithubUsers()

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.users.observe(this){
            setUserDataList(it)
        }

        mainViewModel.isEmpty.observe(this){
            setEmptyIndicator(it)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
    }

    private fun setUserDataList(users: List<User>) {
        val listUserAdapter = ListUserAdapter(users)
        binding.rvUser.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object: ListUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                goToDetail(data)
            }
        })
    }

    private fun goToDetail(user:User){
        val sendObjectIntent =Intent(this@MainActivity, UserDetail::class.java)
        sendObjectIntent.putExtra(MainActivity.EXTRA_USER,user.login)
        startActivity(sendObjectIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val mMenuInflater = menuInflater
        mMenuInflater.inflate(R.menu.option_menu,menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.searchUser(query)
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
        when (item.itemId){
            R.id.setting -> {
                return true
            }
            else -> return true
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if(isLoading) {
            binding.rvUser.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
        }else {
            binding.rvUser.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun setEmptyIndicator(isEmpty: Boolean){
        if(isEmpty){
            binding.emptyIndicator.root.visibility = View.VISIBLE
        }else {
            binding.emptyIndicator.root.visibility = View.INVISIBLE
        }
    }

    private fun isInternetConected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
    private fun setInternetIndicator(isInternet:Boolean) {
        if(!isInternet){
            binding.noInternetIndicator.root.visibility = View.VISIBLE
        }else {
            binding.noInternetIndicator.root.visibility = View.INVISIBLE
        }

    }


}