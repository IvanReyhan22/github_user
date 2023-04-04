package com.example.githubuser.ui

import android.content.Intent
import android.os.Bundle
<<<<<<< Updated upstream
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.adapter.ListUserAdapter
import com.example.githubuser.data.local.entity.UsersEntity
import com.example.githubuser.databinding.FragmentUserListBinding
import com.example.githubuser.viewmodel.UsersViewModel
import com.example.githubuser.viewmodel.ViewModelFactory
=======
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
>>>>>>> Stashed changes

class UserListFragment : Fragment() {

    companion object {
        var ARG_POSITION = "extra_name"
        var ARG_USERNAME = "extra_description"
    }

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!
<<<<<<< Updated upstream
=======
    private val factory: UsersViewModelFactory by lazy {
        UsersViewModelFactory.getInstance(
            requireActivity().applicationContext
        )
    }
    private val viewModel: UsersViewModel by viewModels { factory }
>>>>>>> Stashed changes

    private var position: Int = 0
    private var username: String = ""

<<<<<<< Updated upstream
    private lateinit var myObserver: Observer<com.example.githubuser.data.Result<List<UsersEntity>>>
    private val factory: ViewModelFactory by lazy { ViewModelFactory.getInstance(requireActivity().applicationContext) }
    private val viewModel: UsersViewModel by viewModels {
        factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
=======
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
>>>>>>> Stashed changes
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME).toString()
        }

<<<<<<< Updated upstream
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

        Log.e(
            "ON VIEW CREATED ${position}",
            viewModel.getUserFollow(username, position).hasObservers().toString()
        )
//        if (!viewModel.getUserFollow(username, position).hasObservers()) viewModel.getUserFollow(
//            username,
//            position
//        ).observe(viewLifecycleOwner, myObserver)
        binding.rvUser.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun setUserDataList(users: List<UsersEntity>) {
=======
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
>>>>>>> Stashed changes
        val listAdapter = ListUserAdapter(users)
        binding.rvUser.adapter = listAdapter

        listAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
<<<<<<< Updated upstream

            override fun onItemClicked(data: UsersEntity) {
                toDetail(data)
            }
        })
    }

    private fun toDetail(user: UsersEntity) {
        val sendObjectIntent = Intent(activity, UserDetailActivity::class.java)
        sendObjectIntent.putExtra(HomeActivity.EXTRA_USER, user.username)
        startActivity(sendObjectIntent)
    }


=======
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

>>>>>>> Stashed changes
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

<<<<<<< Updated upstream
    private fun showToast(error: String) {
        Toast.makeText(requireActivity(), error, Toast.LENGTH_SHORT).show()
    }

=======
>>>>>>> Stashed changes
    private fun setEmptyIndicator(isEmpty: Boolean) {
        if (isEmpty) {
            binding.emptyIndicator.root.visibility = View.VISIBLE
        } else {
            binding.emptyIndicator.root.visibility = View.INVISIBLE
        }
    }
<<<<<<< Updated upstream

    override fun onPause() {
        super.onPause()
        Log.e("ON PAUSE ${position}", "PAUSED")
//        viewModel.getUserFollow(username, position).removeObserver(myObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("ON DESTROY ${position}", "DESTROYED")
        _binding = null
    }
=======
>>>>>>> Stashed changes
}