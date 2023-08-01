package com.example.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.User
import com.example.githubuser.adapter.ListUserAdapter
import com.example.githubuser.databinding.FragmentUserListBinding
import com.example.githubuser.ui.model.UsersViewModel
import com.example.githubuser.ui.model.UsersViewModelFactory

class UserListFragment : Fragment() {

    companion object {
        var ARG_POSITION = "extra_name"
        var ARG_USERNAME = "extra_description"
    }

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!
    private val factory: UsersViewModelFactory by lazy {
        UsersViewModelFactory.getInstance(
            requireActivity().applicationContext
        )
    }
    private val viewModel: UsersViewModel by viewModels { factory }

    private var position: Int = 0
    private var username: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME).toString()
        }

        val followType = if (position == 1) {
            "followers"
        } else {
            "following"
        }

        viewModel.getUserFollow(username, followType)
        binding.emptyIndicator.emptyMessage.text =
            if (followType.equals("followers", ignoreCase = true)) {
                getString(R.string.follower_empty)
            } else {
                getString(R.string.following_empty)
            }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.isEmpty.observe(viewLifecycleOwner) {
            setEmptyIndicator(it)
        }

        viewModel.users.observe(viewLifecycleOwner) {
            setUserDataList(it)
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUser.layoutManager = layoutManager

    }

    private fun setUserDataList(users: List<User>) {
        val listAdapter = ListUserAdapter(users)
        binding.rvUser.adapter = listAdapter

        listAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                goToDetail(data)
            }

        })
    }

    private fun goToDetail(user: User) {
        val sendObjectIntent = Intent(requireContext(), UserDetailActivity::class.java)
        sendObjectIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        sendObjectIntent.putExtra(HomeActivity.EXTRA_USER, user.login)
        startActivity(sendObjectIntent)
        requireActivity().finish()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun setEmptyIndicator(isEmpty: Boolean) {
        if (isEmpty) {
            binding.emptyIndicator.root.visibility = View.VISIBLE
        } else {
            binding.emptyIndicator.root.visibility = View.INVISIBLE
        }
    }
}