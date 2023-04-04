package com.example.githubuser.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubuser.ui.UserListFragment

<<<<<<< Updated upstream
class SectionsPagerAdapter(activity: AppCompatActivity) :
    FragmentStateAdapter(activity) {
=======
class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
>>>>>>> Stashed changes
    var username: String = ""

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = UserListFragment()
        fragment.arguments = Bundle().apply {
            putInt(UserListFragment.ARG_POSITION, position + 1)
            putString(UserListFragment.ARG_USERNAME, username)
        }

        return fragment
    }

}