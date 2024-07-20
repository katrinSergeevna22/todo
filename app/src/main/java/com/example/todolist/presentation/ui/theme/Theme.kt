package com.example.todolist.presentation.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Colors.White,
    secondary = Colors.DarkThemeSecondaryColor,
    tertiary = Colors.DarkThemeTertiaryColor,
    background = Colors.DarkThemeBackPrimaryColor,
    surface = Colors.DarkThemeBackSecondaryColor,
    error = Colors.Red,
    onPrimary = Colors.White,
    onSecondary = Colors.White,
    onBackground = Colors.DarkThemeBackElevatedColor,
    onSurface = Colors.DarkThemeSeparatorColor,
    onTertiary = Colors.DarkThemeGrayColor,
    surfaceTint = Colors.Blue
)

private val LightColorScheme = lightColorScheme(
    primary = Color.Black,
    secondary = Colors.LightThemeSecondaryColor,
    tertiary = Colors.LightThemeTertiaryColor,
    background = Colors.LightThemeBackPrimaryColor,
    surface = Colors.LightThemeBackSecondaryColor,
    error = Colors.Red,
    onPrimary = Colors.Black,
    onSecondary = Colors.LightThemeOverlayColor,
    onBackground = Colors.LightThemeBackElevatedColor,
    onSurface = Colors.LightThemeSeparatorColor,
    onTertiary = Colors.LightThemeGrayColor,
    surfaceTint = Colors.Blue
)


@Composable
fun ToDoListComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}