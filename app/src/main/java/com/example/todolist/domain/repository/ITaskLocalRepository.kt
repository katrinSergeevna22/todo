package com.example.todolist.domain.repository

import com.example.todolist.data.network.util.TodoItemScreenUiState
import com.example.todolist.domain.Relevance
import com.example.todolist.domain.model.DataState
import com.example.todolist.domain.model.TodoModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface ITaskLocalRepository {
    private val _remoteDataFlow: MutableStateFlow<TodoItemScreenUiState>
        get() = MutableStateFlow(TodoItemScreenUiState())
    val remoteDataFlow: StateFlow<TodoItemScreenUiState>
        get() = _remoteDataFlow.asStateFlow()

    fun getTasks(): Flow<DataState<List<TodoModel>>>

    suspend fun addTask(text: String, priority: Relevance, deadline: Long?)

    suspend fun updateTask(task: TodoModel)

    suspend fun deleteTask(task: TodoModel)
}