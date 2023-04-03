package com.example.githubuser.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class UsersEntity(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey
    val id: Long,

    @field:ColumnInfo(name = "username")
    val username: String,

    @field:ColumnInfo(name = "name")
    val name: String? = null,

    @field:ColumnInfo(name = "avatarUrl")
    val avatarUrl: String? = null,

    @field:ColumnInfo(name = "location")
    var location: String? = null,

    @field:ColumnInfo(name = "followers")
    var followers: Int? = null,

    @field:ColumnInfo(name = "following")
    var following: Int? = null,

    @field:ColumnInfo(name = "repositories")
    var repositories: Int? = null,

    @field:ColumnInfo(name = "favorite")
    var isfavorite: Boolean
)