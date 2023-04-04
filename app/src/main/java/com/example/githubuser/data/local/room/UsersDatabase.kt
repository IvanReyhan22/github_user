package com.example.githubuser.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.githubuser.data.local.entity.UsersEntity

@Database(entities = [UsersEntity::class], version = 1, exportSchema = false)
<<<<<<< Updated upstream
abstract class UsersDatabase: RoomDatabase() {
    abstract fun usersDao(): UsersDao

    companion object {
        private var instance: UsersDatabase? = null

=======
abstract class UsersDatabase : RoomDatabase() {
    abstract fun usersDao(): UsersDao

    companion object {
        @Volatile
        private var instance: UsersDatabase? = null
>>>>>>> Stashed changes
        fun getInstance(context: Context): UsersDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
<<<<<<< Updated upstream
                    UsersDatabase::class.java,
                    "Users.db"
=======
                    UsersDatabase::class.java, "Users.db"
>>>>>>> Stashed changes
                ).build()
            }
    }
}