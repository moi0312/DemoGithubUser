package com.edlo.mydemoapp.repository.local

import androidx.room.*
import com.edlo.mydemoapp.repository.net.github.data.GithubUserData

@Dao
interface GithubUserDao {
    @Query("SELECT *, rowid FROM table_github_user")
    suspend fun getAll(): List<GithubUserData>

    @Query("SELECT *, rowid FROM table_github_user WHERE login LIKE (:login)")
    suspend fun findByLogin(login: String): List<GithubUserData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<GithubUserData>)

    @Update
    suspend fun update(user: GithubUserData)

    @Delete
    fun delete(user: GithubUserData)
}