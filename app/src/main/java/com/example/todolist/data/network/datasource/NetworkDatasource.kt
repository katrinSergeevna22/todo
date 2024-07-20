package com.example.todolist.data.network.datasource

import android.util.Log
import com.example.todolist.domain.INetworkDatasource
import com.example.todolist.data.network.util.forJson
import com.example.todolist.data.network.util.toModel
import com.example.todolist.data.network.util.toToken
import com.example.todolist.domain.model.TodoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import com.example.todolist.data.network.api.ToDoApi
import com.example.todolist.data.network.dto.request.TodoItemListRequestForJson
import com.example.todolist.data.network.dto.request.TodoItemRequestForJson
import com.example.todolist.data.network.dto.response.TodoItemForJson
import com.example.todolist.data.network.util.NetworkState
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Retrofit data source
 * */
@Singleton
class NetworkDatasource @Inject constructor(
    private val api: ToDoApi
) : INetworkDatasource {

    override fun getTasks(token: String): Flow<NetworkState<List<TodoModel>>> = flow {
        Log.d("Shared", token)
        emit(NetworkState.Loading)
        try {
            Log.d("responseApi", api.toString())
            val response = api.getTasks(token.toToken())
            Log.d("response", response.list.toString())
            emit(
                NetworkState.Success(
                    response.list.map(TodoItemForJson::toModel),
                    response.revision
                )
            )
        } catch (e: Exception) {
            emit(NetworkState.Failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun updateListOfTodoItem(
        tasks: List<TodoModel>, revision: Int, token: String, login: String
    ): Flow<NetworkState<List<TodoModel>>> = flow {
        emit(NetworkState.Loading)
        try {
            val response = api.patchTasks(
                token.toToken(),
                revision,
                TodoItemListRequestForJson(tasks.map { it.forJson(login) })
            )
            emit(
                NetworkState.Success(
                    response.list.map(TodoItemForJson::toModel),
                    response.revision
                )
            )
        } catch (e: Exception) {
            emit(NetworkState.Failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun addTask(
        task: TodoModel,
        revision: Int,
        token: String,
        login: String
    ): Flow<NetworkState<TodoModel>> = flow {
        emit(NetworkState.Loading)
        try {
            val response = api.postTask(
                token.toToken(),
                revision,
                TodoItemRequestForJson(task.forJson(login))
            )
            emit(NetworkState.OK(response.revision))
        } catch (e: Exception) {
            emit(NetworkState.Failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun updateTask(
        task: TodoModel,
        revision: Int,
        token: String,
        login: String
    ): Flow<NetworkState<TodoModel>> = flow {
        emit(NetworkState.Loading)
        try {
            val response = api.putTask(
                token.toToken(),
                revision,
                task.id,
                TodoItemRequestForJson(task.forJson(login))
            )
            emit(NetworkState.OK(response.revision))
        } catch (e: Exception) {
            emit(NetworkState.Failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun deleteTask(
        taskId: UUID,
        revision: Int,
        token: String
    ): Flow<NetworkState<TodoModel>> = flow {
        emit(NetworkState.Loading)
        try {
            Log.d("Net_Source_delete", revision.toString())
            Log.d("Net_Source_delete", taskId.toString())
            val response = api.deleteTask(token.toToken(), revision, taskId)
            emit(NetworkState.OK(response.revision))
        } catch (e: Exception) {
            emit(NetworkState.Failure(e))
        }
    }.flowOn(Dispatchers.IO)

}

