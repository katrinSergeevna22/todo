package com.example.todolist.presentation.ui.theme

import com.example.todolist.R
import com.example.todolist.domain.Relevance

enum class ThemeOption {
    Light, Dark, System
}
fun ThemeOption.textName(): Int {
    return when (this) {
        ThemeOption.Light -> R.string.lightTheme
        ThemeOption.Dark -> R.string.darkTheme
        ThemeOption.System -> R.string.systemTheme
    }
}