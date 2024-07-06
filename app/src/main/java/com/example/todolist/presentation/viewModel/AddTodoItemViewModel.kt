package com.example.todolist.presentation.viewModel

import android.content.Context
import android.os.Message
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.repository.SettingsParameters
import com.example.todolist.data.repository.TaskRepository
import com.example.todolist.domain.Relevance
import com.example.todolist.domain.model.TodoItem
import com.example.todolist.domain.textName
import com.example.todolist.domain.textNameForJson
import com.example.todolist.data.network.util.AddItemScreenUIState
import com.example.todolist.data.network.util.TodoItemScreenUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.todolist.data.network.util.NetworkState
import com.example.todolist.data.repository.SharedPreferencesProvider.context
import com.example.todolist.domain.VariantFunction
import kotlinx.coroutines.flow.StateFlow
import retrofit2.HttpException
import java.io.IOException
import java.lang.Error
import java.net.UnknownHostException

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

/**
 * AddTodoItemViewModel is the business logic of adding or editing elements
 * @param repositoru Managing the data received from the server
 * */
class AddTodoItemViewModel(
    val repository: TaskRepository
) : ViewModel() {

    private val _addTodoUiState = MutableStateFlow(AddItemScreenUIState())
    private val addTodoUiState = _addTodoUiState.asStateFlow()
    fun getAddTodoUiState(): StateFlow<AddItemScreenUIState>{
        return addTodoUiState
    }
    fun loadInState(item: TodoItem?) {
        try {
            if (item != null) {
                _addTodoUiState.value = addTodoUiState.value.copy(
                    id = item.id,
                    description = item.text,
                    relevance = getNumberImportance(item.relevance, context),
                    deadline = item.deadline,
                    executionFlag = item.executionFlag,
                    dateOfCreating = item.dateOfCreating,
                    dateOfEditing = item.dateOfEditing

                )
            }
        } catch (e: IOException) {
            _todoItemsScreenUiState.value =
                todoItemsScreenUiState.value.copy(lastOperation = VariantFunction.FETCH)
            _todoItemsScreenUiState.value =
                todoItemsScreenUiState.value.copy(errorMessage = "Ошибка ввода-вывода: ${e.message}")
        } catch (e: HttpException) {
            _todoItemsScreenUiState.value =
                todoItemsScreenUiState.value.copy(lastOperation = VariantFunction.FETCH)
            _todoItemsScreenUiState.value =
                todoItemsScreenUiState.value.copy(errorMessage = "Ошибка HTTP: ${e.message}")
        } catch (e: UnknownHostException) {
            _todoItemsScreenUiState.value =
                todoItemsScreenUiState.value.copy(lastOperation = VariantFunction.FETCH)
            _todoItemsScreenUiState.value =
                todoItemsScreenUiState.value.copy(errorMessage = "Неизвестный хост: ${e.message}")
        }
    }

    private val _todoItemsScreenUiState = MutableStateFlow(TodoItemScreenUiState())
    private val todoItemsScreenUiState = _todoItemsScreenUiState.asStateFlow()
    fun getTodoItemsScreenUiState(): StateFlow<TodoItemScreenUiState> {
        return todoItemsScreenUiState
    }

    init {
        fetchFlow()
    }

    private fun fetchFlow() {
        viewModelScope.launch {
            repository.getTasksFlow().collect {
                _todoItemsScreenUiState.value = it
            }
        }
        updateTasks()
    }

    fun getCurrentDate(): SimpleDateFormat {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
        return dateFormat
    }

    fun setText(text: String) {
        _addTodoUiState.value = addTodoUiState.value.copy(
            description = text
        )
    }

    fun getNumberImportance(title: String, context: Context): Int {
        val number =
            when (title) {
                Relevance.ORDINARY.textNameForJson(context) -> 0
                Relevance.LOW.textNameForJson(context) -> 1
                Relevance.URGENT.textNameForJson(context) -> 2

                else -> -1
            }
        return number
    }

    fun getTitleImportance(selectedImportance: Int, context: Context): String {
        val titleImportance =
            when (selectedImportance) {
                -1, 0 -> Relevance.ORDINARY.textName(context)
                1 -> Relevance.LOW.textName(context)
                else -> Relevance.URGENT.textName(context)
            }
        return titleImportance
    }

    fun setImportant(index: Int) {
        _addTodoUiState.value = addTodoUiState.value.copy(
            relevance = index
        )
    }

    fun setDeadline(deadline: Long?) {
        _addTodoUiState.value = addTodoUiState.value.copy(
            deadline = deadline
        )
    }

    fun textIsNotEmpty(): Boolean {
        return addTodoUiState.value.description != ""
    }

    fun saveItemByButton(
        item: TodoItem?,
        context: Context
    ) {
        Log.d("SelectItem", item.toString())
        val dataOfNewItem = addTodoUiState.value
        val newItem = TodoItem(
            dataOfNewItem.id,
            dataOfNewItem.description,
            if (dataOfNewItem.relevance == 0) Relevance.ORDINARY.textNameForJson(context)
            else if (dataOfNewItem.relevance == 1) Relevance.LOW.textNameForJson(context)
            else Relevance.URGENT.textNameForJson(context),
            deadline = dataOfNewItem.deadline ?: 0L,
            executionFlag = dataOfNewItem.executionFlag,
            dateOfCreating = if (dataOfNewItem.dateOfCreating == 0L) Calendar.getInstance().timeInMillis
            else
                dataOfNewItem.dateOfCreating,
            dateOfEditing = dataOfNewItem.dateOfEditing ?: Calendar.getInstance().timeInMillis,
        )
        if (item == TodoItem() || item == null) {
            addFlow(newItem)
        } else {
            updateFlow(newItem)
        }
    }

    fun formatDate(date: Long?): String {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return dateFormat.format(Date(date ?: 0L))
    }

    private fun updateTasks() {
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
    private fun addFlow(item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTaskFlow(item)
        }
        updateTasks()
    }

    fun removeFlow(item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeTaskFlow(item)
        }
        updateTasks()
    }

    private fun updateFlow(item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.editTaskFlow(item)
        }
        updateTasks()
    }
}