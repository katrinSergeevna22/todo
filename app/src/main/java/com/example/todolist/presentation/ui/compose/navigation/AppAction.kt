package com.example.todolist.presentation.ui.compose.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.todolist.presentation.ui.compose.divKit.delView
import java.util.UUID

/**
 * Actions transmitted with navigation in the app
 * @param navController Manages app navigation
 * */
class AppActions(navController: NavController) {
    val selectedItem: (UUID?) -> Unit = { itemId ->
        navController.navigate("${AppDestination.ADD_TODO_LIST}/${itemId}")
    }
    val onBack: () -> Unit = {
        navController.navigateUp()
    }

    val onBackFromDivKit: () -> Unit = {
        Log.d("AppAction", "")

        delView()
        navController.navigateUp()
    }

    val onSettingScreen: () -> Unit = {
        navController.navigate(AppDestination.SETTING_SCREEN)
    }
    val onAboutAppScreen: () -> Unit = {
        navController.navigate(AppDestination.ABOUT_APP_SCREEN)
    }
}