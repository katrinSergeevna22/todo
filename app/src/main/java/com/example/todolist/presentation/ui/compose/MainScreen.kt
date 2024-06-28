package com.example.todolistcompose.ui

import AddTodoItemScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todolist.domain.TodoItem
import com.example.todolist.presentation.viewModel.ListViewModel

val viewModel = ListViewModel.newInstance()
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "todo_list") {
        composable("todo_list") { TodoListScreen(
            //viewModel.getTodoList(),
            navController
        ) }
        composable("add_todo_item/{itemId}") { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")
            val item = itemId?.let { viewModel.getItemById(it) }
            AddTodoItemScreen(item, viewModel.size, navController)
        }

        composable("add_todo_item") { AddTodoItemScreen(
            TodoItem(),
            viewModel.size,
            navController
        ) }

    }
}