package com.example.githubuser

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("users")
    fun getUsers(): Call<List<User>>

    @GET("search/users")
    fun searchUser(
        @Query("q") username: String
    ): Call<GithubResponse>

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<User>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<List<User>>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<List<User>>
}