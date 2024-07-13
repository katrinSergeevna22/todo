package com.example.todolist.domain

import com.example.todolist.domain.model.TodoModel
import kotlinx.coroutines.flow.Flow
import com.example.todolist.data.network.util.NetworkState
import java.util.UUID

interface INetworkDatasource {
    fun getTasks(
        token: String
    ): Flow<NetworkState<List<TodoModel>>>

    fun updateListOfTodoItem(
        tasks: List<TodoModel>,
        revision: Int,
        token: String,
        login: String
    ): Flow<NetworkState<List<TodoModel>>>

    fun addTask(
        task: TodoModel,
        revision: Int,
        token: String,
        login: String
    ): Flow<NetworkState<TodoModel>>

    fun updateTask(
        task: TodoModel,
        revision: Int,
        token: String,
        login: String
    ): Flow<NetworkState<TodoModel>>

    fun deleteTask(
        taskId: UUID,
        revision: Int,
        token: String,
    ): Flow<NetworkState<TodoModel>>

    companion object {
        fun newInstance() = INetworkDatasource
    }
}