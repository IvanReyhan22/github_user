package com.example.githubuser.viewmodel

import androidx.lifecycle.ViewModel
import com.example.githubuser.data.UsersRepository

class UsersViewModel(private val usersRepository: UsersRepository) : ViewModel() {
    fun getGithubUsers() = usersRepository.getGithubUsers()

    fun searchUser(username: String) = usersRepository.searchUser(username)

    fun getUserDetail(username: String) = usersRepository.getUserDetail(username)

    fun getUserFollow(username: String, pos: Int) = usersRepository.getUserFollow(username, pos)
}