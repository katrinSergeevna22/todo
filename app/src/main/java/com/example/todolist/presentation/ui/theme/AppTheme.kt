package com.example.todolist.presentation.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ColorPalettePreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Светлая тема", modifier = Modifier.padding(16.dp))

        Box(
            modifier = Modifier
                .height(50.dp)
                .padding(bottom = 8.dp)
        ) {
            Row(
                //modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf(
                    Colors.LightThemeSeparatorColor,
                    Colors.LightThemeOverlayColor,
                ).forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(color)
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .height(50.dp)
        ) {
            Row(
                //modifier = Modifier.fillMaxSize().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf(
                    Colors.LightThemePrimaryColor,
                    Colors.LightThemeSecondaryColor,
                    Colors.LightThemeTertiaryColor,
                    Colors.LightThemeDisableColor,
                ).forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(color)
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .height(50.dp)
        ) {
            Row(
                //modifier = Modifier.fillMaxSize().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf(
                    Colors.Black,
                    Colors.White,
                    Colors.Blue,
                    Colors.Red,
                    Colors.Green,
                    Colors.GrayColor,
                    Colors.LightBlue
                ).forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(color)
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .height(50.dp)
        ) {
            Row(
                //modifier = Modifier.fillMaxSize().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf(
                    Colors.LightThemeBackPrimaryColor,
                    Colors.LightThemeBackSecondaryColor,
                    Colors.LightThemeBackElevatedColor,

                ).forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(color)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Темная тема", modifier = Modifier.padding(16.dp))

        Box(
            modifier = Modifier
                .height(50.dp)
                .padding(bottom = 8.dp)
        ) {
            Row(
                //modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf(
                    Colors.DarkThemeSeparatorColor,
                    Colors.DarkThemeOverlayColor,
                ).forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(color)
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .height(50.dp)
        ) {
            Row(
                //modifier = Modifier.fillMaxSize().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf(
                    Colors.DarkThemePrimaryColor,
                    Colors.DarkThemeSecondaryColor,
                    Colors.DarkThemeTertiaryColor,
                    Colors.DarkThemeDisableColor,
                ).forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(color)
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .height(50.dp)
        ) {
            Row(
                //modifier = Modifier.fillMaxSize().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf(
                    Colors.Black,
                    Colors.White,
                    Colors.Blue,
                    Colors.Red,
                    Colors.Green,
                    Colors.GrayColor,
                    Colors.LightBlue
                ).forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(color)
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .height(50.dp)
        ) {
            Row(
                //modifier = Modifier.fillMaxSize().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf(
                    Colors.DarkThemeBackPrimaryColor,
                    Colors.DarkThemeBackSecondaryColor,
                    Colors.DarkThemeBackElevatedColor,

                    ).forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(color)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Текстовые стили", modifier = Modifier.padding(16.dp))
        // Текстовые стили
        Text(
            text = "Large title",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = "Title",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = "BUTTON",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = "Body",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = "Subhead",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview
@Composable
fun ColorPalettePreviewPreview() {
    ColorPalettePreview()
}