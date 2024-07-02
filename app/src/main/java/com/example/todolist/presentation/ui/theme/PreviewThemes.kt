package com.example.todolist.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.todolistcompose.ui.MainScreen

@Preview(showBackground = true)
@Composable
fun LightThemePreview() {
    ToDoListComposeTheme(darkTheme = false) {
        MainScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun DarkThemePreview() {
    ToDoListComposeTheme(darkTheme = true) {
        MainScreen()
    }
}
