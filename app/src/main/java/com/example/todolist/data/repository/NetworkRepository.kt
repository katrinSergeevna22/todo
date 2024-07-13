package com.example.todolist.data.repository

import android.util.Log
import com.example.todolist.R
import com.example.todolist.data.network.util.NetworkState
import com.example.todolist.data.network.util.TodoItemScreenUiState
import com.example.todolist.domain.IDatabaseSource
import com.example.todolist.domain.INetworkDatasource
import com.example.todolist.domain.VariantFunction
import com.example.todolist.domain.model.TodoModel
import com.example.todolist.domain.repository.ISettingRepository
import com.example.todolist.domain.repository.ITaskRepository
import com.example.todolist.errorHandling.IErrorHandling
import com.example.todolist.providers.IStringProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


class NetworkRepository @Inject constructor(
    private val networkSource: INetworkDatasource,
    private val settingParameters: ISettingRepository,
    private val databaseSource: IDatabaseSource,
    private val errorHandling: IErrorHandling,
    private val stringProvider: IStringProvider,
) : ITaskRepository {

    override suspend fun addTaskFlow(task: TodoModel) {
        if (settingParameters.getNotificationEnabled() && settingParameters.getToken() != "") {
            networkSource.addTask(
                task,
                settingParameters.getRevision(),
                settingParameters.getToken(),
                settingParameters.getUsername()
            ).collect {
                when (it) {
                    is NetworkState.Failure -> synchronize()
                    is NetworkState.OK -> settingParameters.setRevision(it.revision)
                    else -> {}
                }
            }
        }
    }

    override suspend fun editTaskFlow(task: TodoModel) {
        if (settingParameters.getNotificationEnabled() && settingParameters.getToken() != "") {
            networkSource.updateTask(
                task,
                settingParameters.getRevision(),
                settingParameters.getToken(),
                settingParameters.getUsername()
            ).collect {
                when (it) {
                    is NetworkState.Failure -> synchronize()
                    is NetworkState.OK -> settingParameters.setRevision(it.revision)
                    else -> {}
                }
            }
        }
    }

    override suspend fun removeTaskFlow(task: TodoModel) {
        if (settingParameters.getNotificationEnabled() && settingParameters.getToken() != "") {
            networkSource.deleteTask(
                task.id,
                settingParameters.getRevision(),
                settingParameters.getToken()
            ).collect {
                when (it) {
                    is NetworkState.Failure -> synchronize()
                    is NetworkState.OK -> settingParameters.setRevision(it.revision)
                    else -> {}
                }
            }
        }
    }

    override suspend fun synchronize() {
        if (settingParameters.getNotificationEnabled() && settingParameters.getToken() != "") {
            networkSource.getTasks(settingParameters.getToken()).collect { state ->
                when (state) {
                    is NetworkState.Failure -> errorHandling.showException(
                        stringProvider.getString(
                            R.string.errorSynchronized
                        )
                    )

                    is NetworkState.Success -> merge(state.revision, state.data)
                    else -> {}
                }
            }
        }
    }

    private suspend fun merge(
        serverRevision: Int,
        serverData: List<TodoModel>
    ) {
        val localRevision: Int = settingParameters.getRevision()
        val localData: List<TodoModel> = databaseSource.getTasksAsList()
        val data: List<TodoModel> = mergeData(localRevision, localData, serverRevision, serverData)
        networkSource.updateListOfTodoItem(
            data,
            settingParameters.getRevision(),
            settingParameters.getToken(),
            settingParameters.getUsername()
        ).collect { state ->
            when (state) {
                is NetworkState.Failure -> errorHandling.showException(
                    stringProvider.getString(
                        R.string.errorSynchronized
                    )
                )

                is NetworkState.Success -> {
                    databaseSource.overwrite(state.data)
                    settingParameters.setRevision(state.revision)
                }

                else -> {}
            }
        }
    }

    private fun mergeData(
        localRevision: Int,
        localData: List<TodoModel>,
        serverRevision: Int,
        serverData: List<TodoModel>
    ): List<TodoModel> {
        val isLocalDataActual: Boolean = localRevision >= serverRevision
        return listOf(
            localData.map { Pair(true, it) },
            serverData.map { Pair(false, it) }
        ).flatten().groupBy { it.second.id }.map {
            when (it.value.size) {
                1 -> when (it.value.first().first == isLocalDataActual) {
                    true -> it.value.first().second
                    else -> null
                }

                else -> it.value.maxByOrNull { pair ->
                    maxOf(
                        pair.second.dateOfCreating,
                        pair.second.dateOfEditing ?: Long.MAX_VALUE
                    )
                }?.second
            }
        }.filterNotNull()
    }


}