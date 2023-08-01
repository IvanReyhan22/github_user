package com.example.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.githubuser.data.local.entity.UsersEntity

@Dao
interface UsersDao {
    @Query("SELECT * FROM users ORDER BY username DESC")
    fun getUsers(): LiveData<List<UsersEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertUser(user: UsersEntity)

    @Query("SELECT * FROM users where username = :username")
    fun findUser(username: String): LiveData<UsersEntity>

    @Update
    fun updateUsers(users: UsersEntity)

    @Query("DELETE FROM users WHERE username = :username")
    fun deleteUser(username:String)
}