package com.example.todolist.presentation.ui.compose.navigation

import AddTodoItemScreen
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todolist.presentation.ui.compose.todo_list.TodoListScreen
import com.example.todolist.presentation.viewModel.AddTodoItemViewModel
import com.example.todolist.presentation.viewModel.ListViewModelProvider
import java.util.UUID

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val actions = remember(navController) { AppActions(navController) }
    val listViewModel = ListViewModelProvider.listViewModel
    NavHost(
        navController = navController,
        startDestination = AppDestination.TODO_LIST
    ) {
        composable(AppDestination.TODO_LIST) {
            TodoListScreen(
                listViewModel,
                actions.selectedItem,
            )
        }
        composable("${AppDestination.ADD_TODO_LIST}/{itemId}") { backStackEntry ->
            val addViewModel = AddTodoItemViewModel(ListViewModelProvider.repository)
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
    }
}
