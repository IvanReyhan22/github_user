package com.example.githubuser.di

import android.content.Context
import com.example.githubuser.data.UsersRepository
import com.example.githubuser.data.local.room.UsersDatabase
import com.example.githubuser.data.remote.retrofit.ApiConfig
import com.example.githubuser.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): UsersRepository{
        val apiService = ApiConfig.getApiService()
        val database = UsersDatabase.getInstance(context)
        val dao = database.usersDao()
        val appExecutors = AppExecutors()
        return UsersRepository.getInstance(apiService,dao,appExecutors)
    }
}