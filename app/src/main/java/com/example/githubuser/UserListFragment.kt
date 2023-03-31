package com.example.githubuser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.databinding.FragmentUserListBinding

class UserListFragment : Fragment() {

    companion object {
        var ARG_POSITION = "extra_name"
        var ARG_USERNAME = "extra_description"
    }
    private lateinit var binding: FragmentUserListBinding
    private val viewModel: MainViewModel by viewModels()

    private var position:Int = 0
    private var username:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME).toString()
        }

        val followType = if(position == 1){
            "followers"
        }else {
            "following"
        }

        viewModel.getUserFollow(username,followType)
        binding.emptyIndicator.emptyMessage.text = "${followType.replaceFirstChar(Char::uppercase)} data is empty"

        viewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }

        viewModel.isEmpty.observe(viewLifecycleOwner){
            setEmptyIndicator(it)
        }

        viewModel.users.observe(viewLifecycleOwner){
            setUserDataList(it)
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUser.layoutManager = layoutManager

    }

    private fun setUserDataList(users:List<User>) {
        val listAdapter = ListUserAdapter(users)
        binding.rvUser.adapter = listAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        if(isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        }else {
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
}