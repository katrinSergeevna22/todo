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
    onPrimary = Colors.White,
    onSecondary = Colors.White,
    onBackground = Colors.DarkThemeBackElevatedColor,
    onSurface = Colors.DarkThemeSeparatorColor,
)

private val LightColorScheme = lightColorScheme(
    primary = Color.Black,
    secondary = Colors.LightThemeSecondaryColor,
    tertiary = Colors.LightThemeTertiaryColor,
    background = Colors.LightThemeBackPrimaryColor,
    surface = Colors.LightThemeBackSecondaryColor,
    onPrimary = Colors.Black,
    onSecondary = Colors.LightThemeOverlayColor,
    onBackground = Colors.LightThemeBackElevatedColor,
    onSurface = Colors.LightThemeSeparatorColor,

)


@Composable
fun ToDoListComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
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