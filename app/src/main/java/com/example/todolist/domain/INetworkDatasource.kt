package com.example.todolist.domain

import com.example.todolist.domain.model.TodoItem
import kotlinx.coroutines.flow.Flow
import com.example.todolist.data.network.util.NetworkState
import java.util.UUID

interface INetworkDatasource {
    fun getTasks(
        token: String
    ): Flow<NetworkState<List<TodoItem>>>

    fun updateListOfTodoItem(
        tasks: List<TodoItem>,
        revision: Int,
        token: String,
        login: String
    ): Flow<NetworkState<List<TodoItem>>>

    fun addTask(
        task: TodoItem,
        revision: Int,
        token: String,
        login: String
    ): Flow<NetworkState<TodoItem>>

    fun updateTask(
        task: TodoItem,
        revision: Int,
        token: String,
        login: String
    ): Flow<NetworkState<TodoItem>>

    fun deleteTask(
        taskId: UUID,
        revision: Int,
        token: String,
    ): Flow<NetworkState<TodoItem>>

    companion object {
        fun newInstance() = INetworkDatasource
    }
}