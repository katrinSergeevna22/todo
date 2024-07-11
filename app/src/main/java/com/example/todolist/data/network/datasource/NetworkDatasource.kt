package com.example.todolist.data.network.datasource

import android.util.Log
import com.example.todolist.domain.INetworkDatasource
import com.example.todolist.data.network.util.forJson
import com.example.todolist.data.network.util.toModel
import com.example.todolist.data.network.util.toToken
import com.example.todolist.domain.model.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import com.example.todolist.data.network.api.ToDoApi
import com.example.todolist.data.network.dto.request.TodoItemListRequestForJson
import com.example.todolist.data.network.dto.request.TodoItemRequestForJson
import com.example.todolist.data.network.dto.response.TodoItemForJson
import com.example.todolist.data.network.util.NetworkState
import retrofit2.Retrofit
import java.util.UUID
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit data source
 * */
class NetworkDatasource : INetworkDatasource {
    private val api: ToDoApi = Retrofit.Builder()
        .baseUrl("https://hive.mrdekk.ru/todo/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()
        )
        .build()
        .create(ToDoApi::class.java)

    override fun getTasks(token: String): Flow<NetworkState<List<TodoItem>>> = flow {
        Log.d("Shared", token)
        emit(NetworkState.Loading)
        try {
            val response = api.getTasks(token.toToken())
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
        tasks: List<TodoItem>, revision: Int, token: String, login: String
    ): Flow<NetworkState<List<TodoItem>>> = flow {
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
        task: TodoItem,
        revision: Int,
        token: String,
        login: String
    ): Flow<NetworkState<TodoItem>> = flow {
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
        task: TodoItem,
        revision: Int,
        token: String,
        login: String
    ): Flow<NetworkState<TodoItem>> = flow {
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
    ): Flow<NetworkState<TodoItem>> = flow {
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
