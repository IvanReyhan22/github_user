package com.example.githubuser

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.databinding.ActivityUserDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetail : AppCompatActivity() {

    private var username:String = ""
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityUserDetailBinding

    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_title_1,
            R.string.tab_title_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = intent.getStringExtra(UserDetail.EXTRA_USER).toString()

        val actionBar = supportActionBar
        actionBar?.title = username.replaceFirstChar(Char::uppercase)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        val viewPager: ViewPager2 = binding.viewpager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabLayout
        TabLayoutMediator(tabs,viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        mainViewModel.getUserDetail(username)
        mainViewModel.user.observe(this){
            setUserData(it)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun setUserData(data: User) {
        Glide.with(binding.userPhoto.context).load(data.avatarUrl).into(binding.userPhoto)
        binding.name.text = data.name
        binding.username.text = data.login
        binding.location.text = data.location
        binding.follower.text = data.followers.toString()
        binding.following.text = data.following.toString()
        binding.publicRepo.text = data.publicRepos.toString()

    }

    private fun showLoading(isLoading: Boolean) {
        if(isLoading) {
            binding.userInfo.visibility = View.INVISIBLE
            binding.profileProgressbar.visibility = View.VISIBLE
        }else {
            binding.userInfo.visibility = View.VISIBLE
            binding.profileProgressbar.visibility = View.INVISIBLE
        }
    }
}