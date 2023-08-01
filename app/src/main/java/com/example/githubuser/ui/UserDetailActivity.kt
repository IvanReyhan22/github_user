package com.example.githubuser.ui

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.adapter.SectionsPagerAdapter
import com.example.githubuser.data.local.entity.UsersEntity
import com.example.githubuser.databinding.ActivityUserDetailBinding
import com.example.githubuser.ui.model.UsersViewModel
import com.example.githubuser.ui.model.UsersViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity(), View.OnClickListener {

    private var username: String = ""
    private var isFavorite: Boolean = false
    private lateinit var binding: ActivityUserDetailBinding
    private val factory: UsersViewModelFactory by lazy { UsersViewModelFactory.getInstance(applicationContext) }
    private val viewModel: UsersViewModel by viewModels { factory }

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

        username = intent.getStringExtra(EXTRA_USER).toString()

        val actionBar = supportActionBar
        actionBar?.title = username.replaceFirstChar(Char::uppercase)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        val viewPager: ViewPager2 = binding.viewpager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabLayout
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        viewModel.getUserDetail(username)

        viewModel.isUserFavorite(username).observe(this) { user ->
            isFavorite = user != null
            controlFollowButton(isFavorite)
        }

        viewModel.user.observe(this) {
            setUserData(it)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.btnFollow.setOnClickListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun setUserData(data: UsersEntity) {
        Glide.with(binding.userPhoto.context).load(data.avatarUrl).into(binding.userPhoto)

        binding.apply {
            name.text = data.name
            username.text = data.username
            location.text = data.location
            follower.text = data.followers.toString()
            following.text = data.following.toString()
            publicRepo.text = data.repositories.toString()
        }

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.userInfo.visibility = View.INVISIBLE
            binding.profileProgressbar.visibility = View.VISIBLE
        } else {
            binding.userInfo.visibility = View.VISIBLE
            binding.profileProgressbar.visibility = View.INVISIBLE
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_follow -> {
                if (isFavorite) {
                    controlFollowButton(false)
                    viewModel.deleteFavorite(username)
                } else {
                    controlFollowButton(true)
                    viewModel.setFavorite(viewModel.user.value!!)
                }
            }
        }
    }

    private fun controlFollowButton(isFavorite: Boolean) {
        Log.e("controlFollowButton", isFavorite.toString())
        if (isFavorite) {
            binding.btnFollow.apply {
                setIconResource(R.drawable.ic_baseline_check_24)
                setIconTintResource(R.color.success_400)
                text = getString(R.string.following)
            }
        } else {
            binding.btnFollow.apply {
                setIconResource(R.drawable.ic_baseline_add_24)
                setIconTintResource(R.color.neutral_500)
                text = getString(R.string.follow)
            }
        }
    }
}