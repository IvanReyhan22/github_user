package com.example.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.githubuser.data.local.entity.UsersEntity

@Dao
interface UsersDao {
    @Query("SELECT * FROM users ORDER BY name DESC")
    fun getUsers(): LiveData<List<UsersEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(news: List<UsersEntity>)

    @Update
    fun update(news: UsersEntity)

    @Query("DELETE FROM users")
    fun deleteAll()

    @Query("SELECT * FROM users where favorite = 1")
    fun getFavoriteUsers(): LiveData<List<UsersEntity>>

    @Query("SELECT EXISTS(SELECT * FROM users WHERE id = :id AND favorite = 1)")
    fun isUsersFavorite(id: String): Boolean

}