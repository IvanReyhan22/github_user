package com.example.githubuser.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.githubuser.data.local.entity.UsersEntity
import com.example.githubuser.data.local.room.UsersDao
import com.example.githubuser.data.remote.response.GithubResponse
import com.example.githubuser.data.remote.response.User
import com.example.githubuser.data.remote.retrofit.ApiService
import com.example.githubuser.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersRepository private constructor(
    private val apiService: ApiService,
    private val usersDao: UsersDao,
    private val appExecutors: AppExecutors,
) {
    private val listResult = MediatorLiveData<Result<List<UsersEntity>>>()
    private val result = MediatorLiveData<Result<UsersEntity>>()

    private val retrofitListLiveData = MutableLiveData<List<UsersEntity>>()
    private val retrofitLiveData = MutableLiveData<User>()

    fun getGithubUsers(): LiveData<Result<List<UsersEntity>>> {
        listResult.value = Result.Loading
        val client = apiService.getUsers()
        client.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    appExecutors.diskIO.execute {
                        val data = toArrayList(response.body())
                        saveToLocal(data)
                    }
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                listResult.value = Result.Error(t.message.toString())
            }
        })

        val localData = usersDao.getUsers()
        listResult.apply { removeSource(localData) }
        listResult.addSource(localData) { newData: List<UsersEntity> ->
            listResult.value = Result.Success(newData)
        }
        return listResult

    }

    fun searchUser(username: String): LiveData<Result<List<UsersEntity>>> {
//        usersResult.value = Result.Loading
        listResult.value = Result.Loading
        val client = apiService.searchUser(username)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                if (response.isSuccessful) {
                    appExecutors.diskIO.execute {
                        val data = toArrayList(response.body()?.items)
                        saveToLocal(data)
                    }
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
//                usersResult.value = Result.Error(t.message.toString())
                listResult.value = Result.Error(t.message.toString())
            }
        })

        val localData = usersDao.getUsers()
//        usersResult.addSource(localData) { newData: List<UsersEntity> ->
//            usersResult.value = Result.Success(newData)
//        }
        listResult.addSource(localData) { newData: List<UsersEntity> ->
            listResult.value = Result.Success(newData)
        }

//        return usersResult
        return listResult
    }

    fun getUserDetail(username: String): LiveData<Result<UsersEntity>> {
        result.value = Result.Loading
        var isFavorite: Boolean = false
        val client = apiService.getUserDetail(username)
        client.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    retrofitLiveData.value = response.body()
                    appExecutors.diskIO.execute {
                        isFavorite = usersDao.isUsersFavorite(response.body()?.id.toString())
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }

        })

        result.addSource(retrofitLiveData) { user: User ->
            result.value = Result.Success(
                UsersEntity(
                    user.id,
                    user.login,
                    user.name,
                    user.avatarUrl,
                    user.location,
                    user.followers,
                    user.following,
                    user.publicRepos,
                    isFavorite
                )
            )
        }

        return result
    }

    fun getUserFollow(username: String, pos: Int): LiveData<Result<List<UsersEntity>>> {
        listResult.value = Result.Loading
        val client: Call<List<User>> = if (pos == 1) {
            apiService.getUserFollowers(username)
        } else {
            apiService.getUserFollowing(username)
        }
        client.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    var data: List<UsersEntity>
                    appExecutors.diskIO.execute {
                        data = toArrayList(response.body())
                        retrofitListLiveData.postValue(data)
                    }
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                listResult.value = Result.Error(t.message.toString())
            }

        })

        listResult.removeSource(retrofitListLiveData)
        listResult.addSource(retrofitListLiveData) { newData: List<UsersEntity> ->
            listResult.value = Result.Success(newData)
        }

        return listResult
    }

    fun getFavorite(): LiveData<List<UsersEntity>> {
        return usersDao.getFavoriteUsers()
    }

    fun setFavorite(user: UsersEntity, isFavorite: Boolean) {
        appExecutors.diskIO.execute {
            user.isfavorite = true
            usersDao.update(user)
        }
    }

    private fun mapUserToRoom(retrofitRes: List<User>): List<UsersEntity> {
        return retrofitRes.map { user ->
            val isFavorite = usersDao.isUsersFavorite(user.id.toString())
            UsersEntity(
                user.id,
                user.login,
                user.name,
                user.avatarUrl,
                user.location,
                user.followers,
                user.following,
                user.publicRepos,
                isFavorite
            )
        }
    }

    private fun toArrayList(data: List<User>?): ArrayList<UsersEntity> {
        val newData = ArrayList<UsersEntity>()
        data?.forEach { user ->
            val isFavorite = usersDao.isUsersFavorite(user.id.toString())
            val mUser = UsersEntity(
                user.id,
                user.login,
                user.name,
                user.avatarUrl,
                user.location,
                user.followers,
                user.following,
                user.publicRepos,
                isFavorite
            )
            newData.add(mUser)
        }
        return newData
    }

    fun saveToLocal(data: ArrayList<UsersEntity>) {
        usersDao.deleteAll()
        usersDao.insert(data)
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