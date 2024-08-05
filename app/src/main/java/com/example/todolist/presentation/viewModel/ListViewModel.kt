package com.example.todolist.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.R
import com.example.todolist.data.network.util.TodoItemScreenUiState
import com.example.todolist.data.repository.RemoteRepository
import com.example.todolist.domain.Relevance
import com.example.todolist.domain.VariantFunction
import com.example.todolist.domain.errorHandling.ErrorHandlingImpl
import com.example.todolist.domain.model.DataState
import com.example.todolist.domain.model.TodoModel
import com.example.todolist.domain.providers.IStringProvider
import com.example.todolist.domain.repository.ISettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

/**
 * ListViewModel is the main ViewModel in the application, which contains all the basic business logic
 * @param repository Managing the data received from the server
 * */
@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: RemoteRepository,
    private val settingParameters: ISettingRepository,
    private val errorHandlingImpl: ErrorHandlingImpl,
    private val stringRecurse: IStringProvider,
) : ViewModel() {

    private val _todoItemsScreenUiState = MutableStateFlow(TodoItemScreenUiState())
    private val todoItemsScreenUiState = _todoItemsScreenUiState.asStateFlow()
    fun getTodoItemsScreenUiState(): StateFlow<TodoItemScreenUiState> {
        return todoItemsScreenUiState
    }

    private var countOfRetry = 0

    init {
        fetchState()
    }

    fun onClickRetry() {
        Log.d("clickRetryVM", todoItemsScreenUiState.value.toString())
        val itemOrNull = todoItemsScreenUiState.value.lastItem
        val lastFunction = todoItemsScreenUiState.value.lastOperation
        clearErrorInDataFlow()
        when (lastFunction) {
            VariantFunction.GET -> {
                fetchState()
            }

            VariantFunction.ADD -> addTodoItem(itemOrNull ?: TodoModel())
            VariantFunction.UPDATE -> updateTasks()
            VariantFunction.DELETE -> removeTodoItem(itemOrNull ?: TodoModel())
            VariantFunction.EDIT -> updateTodoItem(itemOrNull ?: TodoModel())
            else -> Log.d("Error", "Unknown")
        }
    }

    fun retryFunction() {
        Log.d("retryVM", todoItemsScreenUiState.value.toString())
        if (countOfRetry == 0) {
            viewModelScope.launch {
                delay(5000)
                val itemOrNull = todoItemsScreenUiState.value.lastItem
                when (todoItemsScreenUiState.value.lastOperation) {
                    VariantFunction.GET -> fetchState()
                    VariantFunction.ADD -> addTodoItem(itemOrNull ?: TodoModel())
                    VariantFunction.UPDATE -> updateTasks()
                    VariantFunction.DELETE -> removeTodoItem(itemOrNull ?: TodoModel())
                    VariantFunction.EDIT -> updateTodoItem(itemOrNull ?: TodoModel())
                    else -> Log.d("Error", "Unknown")
                }
            }
            countOfRetry++
        } else {
            _todoItemsScreenUiState.value =
                todoItemsScreenUiState.value.copy(lastItem = null, lastOperation = null)
        }
    }

    fun fetchState() {
        viewModelScope.launch {
            repository.getTasks().collect { state ->
                when (state) {
                    is DataState.Result -> {
                        _todoItemsScreenUiState.emit(
                            repository.remoteDataFlow.value
                        )

                    }

                    is DataState.Exception -> {
                        _todoItemsScreenUiState.emit(
                            repository.remoteDataFlow.value
                        )
                    }

                    else -> {
                        _todoItemsScreenUiState.emit(
                            repository.remoteDataFlow.value
                        )

                    }
                }
            }
        }
    }

    fun updateTasks() {
        val isEnabled = settingParameters.getNotificationEnabled()
        if (isEnabled) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.synchronization()
            }
        } else {
            errorHandlingImpl.showException(stringRecurse.getString(R.string.errorNoInternet))
        }
    }

    fun designOfCheckboxes(executionFlag: Boolean, relevance: Relevance): Int {
        return if (executionFlag) {
            (R.drawable.ic_readliness_flag_checked)
        } else if (relevance == Relevance.URGENT) {
            (R.drawable.ic_readliness_flag_unchecked_high)
        } else {
            (R.drawable.ic_readiness_flag_normal)
        }
    }

    fun getItemById(id: UUID): TodoModel? {
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

    fun clearErrorInDataFlow() {
        repository.clearErrorInDataFlow()
    }

    private fun addTodoItem(item: TodoModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTask(item.text, item.relevance, item.deadline)
        }
    }

    private fun removeTodoItem(item: TodoModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(item)
        }
    }

    fun updateTodoItem(item: TodoModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(item)
        }
    }
}