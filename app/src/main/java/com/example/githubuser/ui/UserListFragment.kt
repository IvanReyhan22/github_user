package com.example.githubuser.ui

import android.content.Intent
import android.os.Bundle
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

class UserListFragment : Fragment() {

    companion object {
        var ARG_POSITION = "extra_name"
        var ARG_USERNAME = "extra_description"
    }

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    private var position: Int = 0
    private var username: String = ""

    private lateinit var myObserver: Observer<com.example.githubuser.data.Result<List<UsersEntity>>>
    private val factory: ViewModelFactory by lazy { ViewModelFactory.getInstance(requireActivity().applicationContext) }
    private val viewModel: UsersViewModel by viewModels {
        factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME).toString()
        }

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
        val listAdapter = ListUserAdapter(users)
        binding.rvUser.adapter = listAdapter

        listAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {

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


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun showToast(error: String) {
        Toast.makeText(requireActivity(), error, Toast.LENGTH_SHORT).show()
    }

    private fun setEmptyIndicator(isEmpty: Boolean) {
        if (isEmpty) {
            binding.emptyIndicator.root.visibility = View.VISIBLE
        } else {
            binding.emptyIndicator.root.visibility = View.INVISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e("ON RESUME ${position}", "RESUMED")
    }

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
}