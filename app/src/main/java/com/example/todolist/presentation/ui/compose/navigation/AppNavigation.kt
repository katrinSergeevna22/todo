package com.example.todolist.presentation.ui.compose.navigation

import AddTodoItemScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todolist.presentation.ui.compose.divKit.ViewScreen
import com.example.todolist.presentation.ui.compose.setting.SettingScreen
import com.example.todolist.presentation.ui.compose.todo_list.TodoListScreen
import com.example.todolist.presentation.viewModel.AddTodoItemViewModel
import com.example.todolist.presentation.viewModel.ListViewModel
import com.example.todolist.presentation.viewModel.SettingViewModel
import java.util.UUID

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val actions = remember(navController) { AppActions(navController) }
    val listViewModel: ListViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = AppDestination.TODO_LIST
    ) {
        composable(AppDestination.TODO_LIST) {
            TodoListScreen(
                listViewModel,
                actions.selectedItem,
                actions.onAboutAppScreen,
                actions.onSettingScreen
            )
        }
        composable("${AppDestination.ADD_TODO_LIST}/{itemId}") { backStackEntry ->
            val addViewModel: AddTodoItemViewModel = hiltViewModel()
            val itemId = backStackEntry.arguments?.getString("itemId")
            if (itemId != "null") {
                val item = itemId.let { listViewModel.getItemById(UUID.fromString(it)) }
                AddTodoItemScreen(
                    item,
                    addViewModel,
                    actions.onBack,
                )
            } else {
                AddTodoItemScreen(
                    null,
                    addViewModel,
                    actions.onBack,
                )
            }
        }
        composable(AppDestination.SETTING_SCREEN) {
            val settingViewModel: SettingViewModel = hiltViewModel()
            SettingScreen(
                settingViewModel,
                actions.onBack
            )
        }
        composable(AppDestination.ABOUT_APP_SCREEN) {
            ViewScreen(
                actions.onBackFromDivKit
            )
        }
    }
}
