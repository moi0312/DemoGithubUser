package com.edlo.mydemoapp.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.edlo.mydemoapp.repository.net.github.data.GithubUserData
import dagger.hilt.android.qualifiers.ApplicationContext

@Database(entities = [GithubUserData::class], version = 1, exportSchema = false)
abstract class GithubUserDB : RoomDatabase() {
    companion object {
        val DB_NAME = "db_github_user"
        private var INSTANCE: GithubUserDB? = null

//        private val MIGRATION_1_2 = object: Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                // TODO
//            }
//        }

        fun getDatabase(@ApplicationContext context: Context): GithubUserDB {
            if(INSTANCE == null) {
                INSTANCE = Room.databaseBuilder( context,
                    GithubUserDB::class.java, DB_NAME )
//                .addMigrations(MIGRATION_1_2)
                    .build()
            }
            return INSTANCE!!
        }
    }
    abstract fun githubUserDao(): GithubUserDao
}