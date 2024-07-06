package com.example.todolist.data.repository

import com.example.todolist.domain.model.TodoItem
import com.example.todolist.data.network.util.TodoItemScreenUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import com.example.todolist.data.network.datasource.NetworkDatasource
import com.example.todolist.data.network.util.NetworkState
import com.example.todolist.domain.VariantFunction

/**
 * Repository that processes the data received in requests
 * */
class TaskRepository() {
    private val networkSource = NetworkDatasource()

    private val _dataFlow = MutableStateFlow(TodoItemScreenUiState())
    private val dataFlow = _dataFlow.asStateFlow()

    fun updateTasks(
        tasks: List<TodoItem>,
        revision: Int,
        token: String,
        login: String
    ): Flow<NetworkState<List<TodoItem>>> {
        return networkSource.updateListOfTodoItem(tasks, revision, token, login)
    }

    private val settingParameters: SettingsParameters = SettingsParameters()
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

    suspend fun addTaskFlow(task: TodoItem) {
        withContext(Dispatchers.IO) {
            networkSource.addTask( task, settingParameters.getRevision(),
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
                        errorProvide(it.cause.message, VariantFunction.ADD, task, true)
                    }
                }
            }
        }
    }

    private fun errorProvide(
        message: String?,
        variantFunction: VariantFunction,
        task: TodoItem,
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
                "HTTP 500 Internal Server Error" -> "Внутренняя ошибка сервера!"
                else -> "Ошибка!"
            }
        return errorMessage
    }

    suspend fun removeTaskFlow(task: TodoItem) {
        withContext(Dispatchers.IO) {
            networkSource.deleteTask( task.id, settingParameters.getRevision(),
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
                        errorProvide(it.cause.message, VariantFunction.DELETE, task, true)
                    }
                }
            }
        }
    }

    suspend fun editTaskFlow(task: TodoItem) {
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
                        errorProvide(it.cause.message, VariantFunction.EDIT, task, true)
                    }
                }
            }
        }
    }
    private fun updateParameterList(task: TodoItem) : List<TodoItem>{
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
    fun clearErrorInDataFlow() {
        _dataFlow.value = dataFlow.value.copy(errorMessage = null)
        _dataFlow.value =
            dataFlow.value.copy(lastOperation = null)
        _dataFlow.value =
            dataFlow.value.copy(lastItem = null)
    }
}