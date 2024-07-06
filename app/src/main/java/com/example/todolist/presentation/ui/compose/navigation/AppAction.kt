package com.example.todolist.presentation.ui.compose.navigation

import androidx.navigation.NavController
import java.util.UUID

/**
 * Actions transmitted with navigation in the app
 * @param navController Manages app navigation
 * */
class AppActions(navController: NavController){
    val selectedItem: (UUID?) -> Unit = { itemId ->
        navController.navigate("${AppDestination.ADD_TODO_LIST}/${itemId}")
    }
    val onBack: () -> Unit = {
        navController.navigateUp()
    }
}