package com.example.todolist.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.ContextCompat
import com.example.todolist.R
import com.example.todolist.domain.repository.ISettingRepository
import com.example.todolist.presentation.ui.theme.ThemeOption
import com.example.todolist.presentation.ui.theme.ThemeOption.System
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Shared preferneces data source
 * @see SharedPreferences
 * */
class SettingRepository @Inject constructor(
    @ApplicationContext val context: Context
) : ISettingRepository {
    private companion object {

        const val REVISION: String = "REVISION"
        const val REVISION_DEFAULT: Int = 1

        const val USERNAME: String = "USERNAME"
        const val USERNAME_DEFAULT: String = "Pervuhina Ekaterina"

        const val TOKEN: String = "TOKEN"
        const val TOKEN_DEFAULT: String = ""

        const val NOTIFICATION_ENABLED: String = "NOTIFICATION_ENABLED"
        const val NOTIFICATION_ENABLED_DEFAULT: Boolean = true

        const val SELECT_THEME: String = "SELECT_THEME"
        const val SELECT_THEME_DEFAULT: Int = 2
    }

    private val source: SharedPreferences = context.getSharedPreferences(
        ContextCompat.getString(
            context, R.string.name
        ), Context.MODE_PRIVATE
    )

    override fun getRevision(): Int = source.getInt(REVISION, REVISION_DEFAULT)

    override fun setRevision(revision: Int) {
        source.edit().putInt(REVISION, revision).apply()
    }

    override fun getUsername(): String =
        source.getString(USERNAME, USERNAME_DEFAULT) ?: USERNAME_DEFAULT

    override fun getToken(): String = source.getString(TOKEN, TOKEN_DEFAULT) ?: TOKEN_DEFAULT

    override fun getNotificationEnabled(): Boolean =
        source.getBoolean(NOTIFICATION_ENABLED, NOTIFICATION_ENABLED_DEFAULT)

    override fun getFlowNotificationEnabled(): StateFlow<Boolean> =
        MutableStateFlow( source.getBoolean(NOTIFICATION_ENABLED, NOTIFICATION_ENABLED_DEFAULT) )

    override fun setNotificationEnabled(mode: Boolean) {
        source.edit().putBoolean(NOTIFICATION_ENABLED, mode).apply()
    }

    override fun getSelectedTheme(): Int = source.getInt(SELECT_THEME, SELECT_THEME_DEFAULT)

    override fun setSelectedTheme(themeOption: Int) {
        source.edit().putInt(SELECT_THEME, themeOption).apply()
    }
}