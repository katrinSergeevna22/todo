package com.example.todolist.presentation.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.todolist.presentation.ui.compose.navigation.AppNavigation

@Preview(showBackground = true)
@Composable
fun LightThemePreview() {
    ToDoListComposeTheme(darkTheme = false) {
        AppNavigation()
    }
}

@Preview(showBackground = true)
@Composable
fun DarkThemePreview() {
    ToDoListComposeTheme(darkTheme = true) {
        AppNavigation()
    }
}

