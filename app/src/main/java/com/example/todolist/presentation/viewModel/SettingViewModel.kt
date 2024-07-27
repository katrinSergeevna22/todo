package com.example.todolist.presentation.viewModel

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import com.example.todolist.domain.providers.IStringProvider
import com.example.todolist.domain.repository.ISettingRepository
import com.example.todolist.presentation.ui.theme.ThemeOption
import com.example.todolist.presentation.ui.theme.textName
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingParameters: ISettingRepository,
    private val stringRecurse: IStringProvider,
) : ViewModel() {
    fun getTheme(): ThemeOption {
        return ThemeOption.entries[settingParameters.getSelectedTheme()]
    }

    private fun setTheme(themeOption: ThemeOption) {
        settingParameters.setSelectedTheme(themeOption.ordinal)
    }

    fun getTitleItems(themeOption: ThemeOption): String {
        Log.d("SVMText", themeOption.toString())
        return stringRecurse.getString(themeOption.textName())
    }

    fun setAppTheme(themeOption: ThemeOption) {
        setTheme(themeOption)
        when (themeOption) {
            ThemeOption.Dark -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }

            ThemeOption.Light -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            ThemeOption.System -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }
}