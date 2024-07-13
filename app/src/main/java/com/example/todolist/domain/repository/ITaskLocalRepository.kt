package com.example.todolist.domain.repository

import com.example.todolist.data.network.util.TodoItemScreenUiState
import com.example.todolist.domain.Relevance
import com.example.todolist.domain.model.DataState
import com.example.todolist.domain.model.TodoModel
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface ITaskLocalRepository {
    fun getTasks(): Flow<DataState<List<TodoModel>>>

    //fun getTask(id: UUID): Flow<TodoItemScreenUiState>

    suspend fun addTask(text: String, priority: Relevance, deadline: Long?)

    suspend fun updateTask(task: TodoModel)

    suspend fun deleteTask(task: TodoModel)
}