package com.example.todolist.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.repository.SettingsParameters
import com.example.todolist.data.repository.TaskRepository
import com.example.todolist.domain.model.TodoItem
import com.example.todolist.data.network.util.TodoItemScreenUiState

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.todolist.data.network.util.NetworkState
import com.example.todolist.domain.VariantFunction
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

/**
 * ListTodoItemViewModel is the main ViewModel in the application, which contains all the basic business logic
 * @param repositoru Managing the data received from the server
 * */
class ListViewModel(private val repository: TaskRepository) : ViewModel() {

    private val _todoItemsScreenUiState = MutableStateFlow(TodoItemScreenUiState())
    private val todoItemsScreenUiState = _todoItemsScreenUiState.asStateFlow()
    fun getTodoItemsScreenUiState(): StateFlow<TodoItemScreenUiState> {
        return todoItemsScreenUiState
    }
    private var countOfRetry = 0

    init {
        fetchRepository()
    }

    fun onClickRetry() {
        val itemOrNull = todoItemsScreenUiState.value.lastItem
        val lastFunction = todoItemsScreenUiState.value.lastOperation
        clearErrorInDataFlow()
        when (lastFunction) {
            VariantFunction.FETCH -> {
                fetchRepository()
            }
            VariantFunction.ADD -> addTodoItem(itemOrNull ?: TodoItem())
            VariantFunction.UPDATE -> updateTasks()
            VariantFunction.DELETE -> removeTodoItem(itemOrNull ?: TodoItem())
            VariantFunction.EDIT -> updateTodoItem(itemOrNull ?: TodoItem())
            null -> Log.d("Error", "Unknown")
        }
    }

    fun retryFunction() {
        if (countOfRetry == 0) {
            viewModelScope.launch {
                delay(5000)
                val itemOrNull = todoItemsScreenUiState.value.lastItem
                when (todoItemsScreenUiState.value.lastOperation) {
                    VariantFunction.FETCH -> fetchRepository()
                    VariantFunction.ADD -> addTodoItem(itemOrNull ?: TodoItem())
                    VariantFunction.UPDATE -> updateTasks()
                    VariantFunction.DELETE -> removeTodoItem(itemOrNull ?: TodoItem())
                    VariantFunction.EDIT -> updateTodoItem(itemOrNull ?: TodoItem())
                    null -> Log.d("Error", "Unknown")
                }
            }
            countOfRetry++
        } else {
            _todoItemsScreenUiState.value =
                todoItemsScreenUiState.value.copy(lastItem = null, lastOperation = null)
        }
    }

    fun fetchRepository() {
        viewModelScope.launch {
            repository.fetchTasks()
        }
        fetchFlow()
    }

    private fun fetchFlow() {
        viewModelScope.launch {
            repository.getTasksFlow().collect {
                _todoItemsScreenUiState.value = it
            }
        }
    }

    fun getItemById(id: UUID): TodoItem? {
        return todoItemsScreenUiState.value.currentTodoItemList.find { it.id == id }
    }

    fun formatDate(date: Long?): String {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return dateFormat.format(Date(date ?: 0L))
    }

    fun switchVisibility() {
        val oldVisibility = todoItemsScreenUiState.value.isVisibleDone
        _todoItemsScreenUiState.value =
            todoItemsScreenUiState.value.copy(isVisibleDone = !oldVisibility)
    }


    fun updateTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val settingsSource = SettingsParameters()
                repository.updateTasks( todoItemsScreenUiState.value.allTodoItemsList,
                    settingsSource.getRevision(), settingsSource.getToken(), settingsSource.getUsername()
                ).collect { state ->
                    when (state) {
                        is NetworkState.Failure ->
                            provideFailure(state.cause.message, VariantFunction.UPDATE)
                        is NetworkState.Success -> {
                            settingsSource.setRevision(state.revision)
                            provideSuccess(state.data, true)
                        }
                        is NetworkState.Loading ->
                            _todoItemsScreenUiState.value =
                                todoItemsScreenUiState.value.copy(loading = false)
                        else -> {}
                    }
                }
            } catch (e: IOException) {
                provideError(VariantFunction.UPDATE, e)
            } catch (e: HttpException) {
                provideError(VariantFunction.UPDATE, e)
            } catch (e: UnknownHostException) {
                provideError(VariantFunction.UPDATE, e)
            }
        }
    }
    private fun provideSuccess(list: List<TodoItem>, flag: Boolean){
        _todoItemsScreenUiState.value =
            todoItemsScreenUiState.value.copy(allTodoItemsList = list)
        _todoItemsScreenUiState.value =
            todoItemsScreenUiState.value.copy(loading = true)
    }
    private fun provideFailure(message: String?, variantFunction: VariantFunction){
        _todoItemsScreenUiState.value =
            todoItemsScreenUiState.value.copy(errorMessage = message)
        _todoItemsScreenUiState.value =
            todoItemsScreenUiState.value.copy(lastOperation = variantFunction)
    }

    private fun provideError(variantFunction: VariantFunction, e: Exception){
        _todoItemsScreenUiState.value =
            todoItemsScreenUiState.value.copy(lastOperation = variantFunction)
        when(e){
            is IOException -> _todoItemsScreenUiState.value =
                todoItemsScreenUiState.value.copy(errorMessage = "Ошибка ввода-вывода: ${e.message}")
            is HttpException -> _todoItemsScreenUiState.value =
                todoItemsScreenUiState.value.copy(errorMessage = "Ошибка HTTP: ${e.message}")
            is UnknownHostException -> _todoItemsScreenUiState.value =
                todoItemsScreenUiState.value.copy(errorMessage = "Неизвестный хост: ${e.message}")
        }
    }

    fun editExecutionFlow(item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.editTaskFlow(item)
        }
        updateTasks()

    }

    fun clearErrorInDataFlow() {
        repository.clearErrorInDataFlow()
    }

    private fun addTodoItem(item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTaskFlow(item)
        }
        updateTasks()
    }

    fun removeTodoItem(item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeTaskFlow(item)
        }
        updateTasks()
    }

    private fun updateTodoItem(item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.editTaskFlow(item)
        }
        updateTasks()
    }
}