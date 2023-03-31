package com.example.githubuser

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    companion object {
        private const val TAG = "MainViewModel"
    }

    init {
        _isEmpty.value = false
    }

    fun getGithubUsers(){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers()
        client.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                _isLoading.value = false
                if(response.isSuccessful) {
                    _users.value = response.body()
                    checkIfEmpty()
                }else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG,"OnFailure : ${t.message}")
            }

        })
    }

    fun searchUser(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchUser(username)
        client.enqueue(object: Callback<GithubResponse>{
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    _users.value = response.body()?.items
                    checkIfEmpty()
                }else {
                    Log.e(TAG,"OnFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG,"OnFailure : ${t.message}")
            }

        })
    }

    fun getUserDetail(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                _isLoading.value = false
                if(response.isSuccessful){
                    _user.value = response.body()
                }else {
                    Log.e(TAG,"OnFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG,"OnFailure : ${t.message}")
            }

        })
    }

    fun getUserFollow(username: String,type:String){
        _isLoading.value = true
        val client = if(type == "followers"){
            ApiConfig.getApiService().getUserFollowers(username)
        }else {
            ApiConfig.getApiService().getUserFollowing(username)
        }

        client.enqueue(object: Callback<List<User>>{
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                _isLoading.value = false
                if(response.isSuccessful){
                    _users.value = response.body()
                    checkIfEmpty()
                }else {
                    Log.e(TAG,"OnFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG,"OnFailure : ${t.message}")
            }

        })
    }


    private fun checkIfEmpty() {
        _isEmpty.value = _users.value?.size!! <= 0
    }


}