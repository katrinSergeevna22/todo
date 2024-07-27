package com.example.todolist.domain

import com.example.todolist.data.database.DatabaseState
import com.example.todolist.domain.model.TodoModel
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface IDatabaseSource {
    fun getTasks(): Flow<DatabaseState<List<TodoModel>>>

    fun getTask(uuid: UUID): Flow<DatabaseState<TodoModel>>

    suspend fun addTask(task: TodoModel)

    suspend fun deleteTask(task: TodoModel)

    suspend fun getTasksAsList(): List<TodoModel>

    suspend fun getTaskAsEntity(uuid: UUID): TodoModel?

    fun overwrite(tasks: List<TodoModel>)
}