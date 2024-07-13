package com.example.todolist.data.repository

import com.example.todolist.domain.model.TodoModel
import com.example.todolist.data.network.util.TodoItemScreenUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import com.example.todolist.data.network.util.NetworkState
import com.example.todolist.domain.IDatabaseSource
import com.example.todolist.domain.INetworkDatasource
import com.example.todolist.domain.VariantFunction
import com.example.todolist.domain.repository.ISettingRepository
import com.example.todolist.domain.repository.ITaskRepository
import javax.inject.Inject

/**
 * Repository that processes the data received in requests
 * */

class OldTaskRepository @Inject constructor(
    private val networkSource: INetworkDatasource,
    private val settingParameters: ISettingRepository,
    private val databaseSource: IDatabaseSource,
) : ITaskRepository

//private val database: TasksDatabase,
//private val api: ToDoApi,
//)
{
    //private val networkSource = NetworkDatasource()

    private val _dataFlow = MutableStateFlow(TodoItemScreenUiState())
    private val dataFlow = _dataFlow.asStateFlow()

    fun updateTasks(
        tasks: List<TodoModel>,
        revision: Int,
        token: String,
        login: String
    ): Flow<NetworkState<List<TodoModel>>> {
        return networkSource.updateListOfTodoItem(tasks, revision, token, login)
    }

    suspend fun fetchTasks() {
        withContext(Dispatchers.IO) {
            if (settingParameters.getToken() != "") {
                networkSource.getTasks(settingParameters.getToken()).collect {
                    when (it) {
                        is NetworkState.Loading -> {
                            _dataFlow.value =
                                dataFlow.value.copy(loading = false)
                        }

                        is NetworkState.Success -> {
                            _dataFlow.value =
                                dataFlow.value.copy(allTodoItemsList = it.data)
                            _dataFlow.value =
                                dataFlow.value.copy(loading = true)
                        }

                        is NetworkState.Failure -> {
                            synchronize()
                            _dataFlow.value =
                                dataFlow.value.copy(errorMessage = it.cause.message)
                            _dataFlow.value =
                                dataFlow.value.copy(loading = true)
                        }

                        else -> {
                            _dataFlow.value =
                                dataFlow.value.copy(allTodoItemsList = emptyList())
                        }
                    }
                }
            }
        }
    }

    fun getTasksFlow(): Flow<TodoItemScreenUiState> {
        return dataFlow
    }

    override suspend fun addTaskFlow(task: TodoModel) {
        withContext(Dispatchers.IO) {
            networkSource.addTask(
                task, settingParameters.getRevision(),
                settingParameters.getToken(), settingParameters.getUsername()
            ).collect {
                when (it) {
                    is NetworkState.Loading -> {
                        _dataFlow.value = dataFlow.value.copy(loading = false)
                    }

                    is NetworkState.OK -> {
                        val updatedList = _dataFlow.value.allTodoItemsList.toMutableList()
                        updatedList.add(task)
                        _dataFlow.value = dataFlow.value.copy(
                            allTodoItemsList = updatedList, loading = true
                        )
                    }

                    is NetworkState.Success -> {
                        val updatedList = _dataFlow.value.allTodoItemsList.toMutableList()
                        updatedList.add(task)
                        _dataFlow.value = dataFlow.value.copy(
                            allTodoItemsList = updatedList, loading = true
                        )
                    }

                    is NetworkState.Failure -> {
                        synchronize()
                        errorProvide(it.cause.message, VariantFunction.ADD, task, true)
                    }
                }
            }
        }
    }

    private fun errorProvide(
        message: String?,
        variantFunction: VariantFunction,
        task: TodoModel,
        flag: Boolean
    ) {
        _dataFlow.value =
            dataFlow.value.copy(errorMessage = translateError(message))
        _dataFlow.value =
            dataFlow.value.copy(lastOperation = variantFunction)
        _dataFlow.value =
            dataFlow.value.copy(lastItem = task)
        _dataFlow.value = dataFlow.value.copy(loading = flag)
    }

    private fun translateError(error: String?): String {
        val errorMessage =
            when (error) {
                "HTTP 400 Bad Request" -> "Ошибка! Некорректный запрос! Попробуйте обновить данные!"
                "HTTP 401 Unauthorized" -> "Ошибка! Неверная авторизация!"
                "HTTP 404 Not Found" -> "Ошибка! Запрашиваемый элемент не найден!"
                "HTTP 500 Internal Server Error" -> "Внутренняя ошибка сервера!"
                else -> "Ошибка!"
            }
        return errorMessage
    }

    override suspend fun removeTaskFlow(task: TodoModel) {
        withContext(Dispatchers.IO) {
            networkSource.deleteTask(
                task.id, settingParameters.getRevision(),
                settingParameters.getToken(),
            ).collect {
                when (it) {
                    is NetworkState.Loading -> {
                        _dataFlow.value =
                            dataFlow.value.copy(loading = false)
                    }

                    is NetworkState.Success -> {
                        val updatedList = _dataFlow.value.allTodoItemsList.toMutableList()
                        updatedList.remove(task)
                        _dataFlow.value = dataFlow.value.copy(
                            allTodoItemsList = updatedList, loading = true
                        )
                    }

                    is NetworkState.OK -> {
                        val updatedList = _dataFlow.value.allTodoItemsList.toMutableList()
                        updatedList.remove(task)
                        _dataFlow.value = dataFlow.value.copy(
                            allTodoItemsList = updatedList, loading = true
                        )
                    }

                    is NetworkState.Failure -> {
                        synchronize()
                        errorProvide(it.cause.message, VariantFunction.DELETE, task, true)
                    }
                }
            }
        }
    }

    override suspend fun editTaskFlow(task: TodoModel) {
        withContext(Dispatchers.IO) {
            networkSource.updateTask(
                task,
                settingParameters.getRevision(),
                settingParameters.getToken(),
                settingParameters.getUsername()
            ).collect {
                when (it) {
                    is NetworkState.Loading -> {
                        _dataFlow.value =
                            dataFlow.value.copy(loading = false)
                    }

                    is NetworkState.OK -> {
                        _dataFlow.value = dataFlow.value.copy(
                            allTodoItemsList = updateParameterList(task), loading = true
                        )
                    }

                    is NetworkState.Success -> {
                        _dataFlow.value = dataFlow.value.copy(
                            allTodoItemsList = updateParameterList(task), loading = true
                        )
                    }

                    is NetworkState.Failure -> {
                        synchronize()
                        errorProvide(it.cause.message, VariantFunction.EDIT, task, true)
                    }
                }
            }
        }
    }

    private fun updateParameterList(task: TodoModel): List<TodoModel> {
        val updatedList = _dataFlow.value.allTodoItemsList.toMutableList()
        updatedList.forEach {
            if (task.id == it.id) {
                it.executionFlag = task.executionFlag
                it.text = task.text
                it.relevance = task.relevance
                it.deadline = task.deadline
                it.dateOfEditing = task.dateOfEditing
            }
        }
        return updatedList
    }


    override suspend fun synchronize() {
        if (settingParameters.getNotificationEnabled()) {
            networkSource.getTasks(settingParameters.getToken()).collect { state ->
                when (state) {
                    //is NetworkState.Failure -> throw NetworkException("Internet is not available!")
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
                //is NetworkState.Failure -> throw NetworkException("Internet is not available!")
                is NetworkState.Success -> {
                    databaseSource.overwrite(state.data)
                    settingParameters.setRevision(state.revision)
                    //localData.forEach(scheduler::cancelNotification)
                    //state.data.forEach(scheduler::scheduleNotification)
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


    fun clearErrorInDataFlow() {
        _dataFlow.value = dataFlow.value.copy(errorMessage = null)
        _dataFlow.value =
            dataFlow.value.copy(lastOperation = null)
        _dataFlow.value =
            dataFlow.value.copy(lastItem = null)
    }
}