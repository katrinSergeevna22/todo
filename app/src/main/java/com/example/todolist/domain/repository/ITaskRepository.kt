package com.example.todolist.domain.repository

import com.example.todolist.domain.model.TodoModel

interface ITaskRepository {
    suspend fun addTaskFlow(task: TodoModel)

    suspend fun editTaskFlow(task: TodoModel)

    suspend fun removeTaskFlow(task: TodoModel)

    suspend fun synchronize()
}