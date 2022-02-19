package com.edlo.mydemoapp.helper

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.edlo.mydemoapp.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPreferencesHelper{
    private enum class Keys {
        STR_SEARCH_KEY,
    }

    companion object {
        private lateinit var instance: SharedPreferences
    }

    @Inject constructor(@ApplicationContext context: Context) {
        instance = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
    }

    fun reset(): SharedPreferencesHelper {
        instance.edit(commit = true) { clear() }
        return this
    }

    var searchKey: String
        get() {
            return try {
                instance.getString(Keys.STR_SEARCH_KEY.name, "")
            } catch (e: ClassCastException) {
                ""
            } as String
        }
        set(value) {
            instance.edit(commit = true) {
                putString(Keys.STR_SEARCH_KEY.name,  value)
            }
        }
}