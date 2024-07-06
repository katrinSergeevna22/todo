package com.example.todolist.data.repository

import android.content.SharedPreferences

/**
 * Shared preferneces data source
 * @see SharedPreferences
 * */
class SettingsParameters() {
    private companion object {
        private val source: SharedPreferences =
            SharedPreferencesProvider.sharedPreferences

        const val REVISION: String = "REVISION"
        const val REVISION_DEFAULT: Int = 1

        const val USERNAME: String = "USERNAME"
        const val USERNAME_DEFAULT: String = "Pervuhina Ekaterina"

        const val TOKEN: String = "TOKEN"
        const val TOKEN_DEFAULT: String = ""
    }


    fun getRevision(): Int = source.getInt(REVISION, REVISION_DEFAULT)

    fun setRevision(revision: Int) {
        source.edit().putInt(REVISION, revision).apply()
    }

    fun getUsername(): String =
        source.getString(USERNAME, USERNAME_DEFAULT) ?: USERNAME_DEFAULT

    fun getToken(): String = source.getString(TOKEN, TOKEN_DEFAULT) ?: TOKEN_DEFAULT

}