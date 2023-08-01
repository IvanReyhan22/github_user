package com.example.githubuser.data

import androidx.lifecycle.LiveData
import com.example.githubuser.data.local.entity.UsersEntity
import com.example.githubuser.data.local.room.UsersDao
import com.example.githubuser.data.remote.ApiService
import com.example.githubuser.utils.AppExecutors

class UsersRepository(
    private val apiService: ApiService,
    private val usersDao: UsersDao,
    private val appExecutors: AppExecutors
) {

    fun getFavoriteUser() : LiveData<List<UsersEntity>> {
        return usersDao.getUsers()
    }

    fun findUser(username: String): LiveData<UsersEntity>{
        return usersDao.findUser(username)
    }

    fun setFavorite(user: UsersEntity){
        appExecutors.diskIO.execute{
            usersDao.insertUser(user)
        }
    }

    fun deleteFavorite(username: String){
        appExecutors.diskIO.execute {
            usersDao.deleteUser(username)
        }
    }

    companion object {
        @Volatile
        private var instance: UsersRepository? = null
        fun getInstance(
            apiService: ApiService,
            newsDao: UsersDao,
            appExecutors: AppExecutors
        ): UsersRepository =
            instance ?: synchronized(this) {
                instance ?: UsersRepository(apiService, newsDao, appExecutors)
            }.also { instance = it }
    }
}