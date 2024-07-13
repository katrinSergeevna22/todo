package com.example.todolist.data.network.util

import com.example.todolist.domain.VariantFunction
import com.example.todolist.domain.model.TodoModel

data class TodoItemScreenUiState(
    val allTodoItemsList: List<TodoModel> = listOf(),
    val isVisibleDone: Boolean = false,
    val errorMessage: String? = null,
    val lastOperation: VariantFunction? = null,
    val lastItem: TodoModel? = null,
    val loading : Boolean = true

) {
    val numberOfDoneTodoItem: Int = allTodoItemsList.count { it.executionFlag }
    val currentTodoItemList =
        if (isVisibleDone) allTodoItemsList.filter { !it.executionFlag } else allTodoItemsList

}