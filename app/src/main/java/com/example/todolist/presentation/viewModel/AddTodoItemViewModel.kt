package com.example.todolist.presentation.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.domain.Relevance
import com.example.todolist.domain.model.TodoModel
import com.example.todolist.domain.textNameForJson
import com.example.todolist.data.network.util.AddItemScreenUIState
import com.example.todolist.data.network.util.TodoItemScreenUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.todolist.data.repository.RemoteRepository
import com.example.todolist.domain.VariantFunction
import com.example.todolist.domain.model.DataState
import com.example.todolist.domain.numberToRelevance
import com.example.todolist.domain.repository.ISettingRepository
import com.example.todolist.providers.IStringProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * AddTodoItemViewModel is the business logic of adding or editing elements
 * @param repositoru Managing the data received from the server
 * */
@HiltViewModel
class AddTodoItemViewModel @Inject constructor(
    //val repository: TaskRepository,
    val repository: RemoteRepository,
    private val settingParameters: ISettingRepository,
    private val stringRecurse: IStringProvider,
) : ViewModel() {

    private val _addTodoUiState = MutableStateFlow(AddItemScreenUIState())
    private val addTodoUiState = _addTodoUiState.asStateFlow()
    fun getAddTodoUiState(): StateFlow<AddItemScreenUIState>{
        return addTodoUiState
    }
    fun loadInState(item: TodoModel?) {
        try {
            if (item != null) {
                _addTodoUiState.value = addTodoUiState.value.copy(
                    id = item.id,
                    description = item.text,
                    relevance = getNumberImportance(stringRecurse.getString(item.relevance.textNameForJson())),
                    deadline = item.deadline,
                    executionFlag = item.executionFlag,
                    dateOfCreating = item.dateOfCreating,
                    dateOfEditing = item.dateOfEditing

                )
            }
        } catch (e: IOException) {
            _todoItemsScreenUiState.value =
                todoItemsScreenUiState.value.copy(lastOperation = VariantFunction.GET)
            _todoItemsScreenUiState.value =
                todoItemsScreenUiState.value.copy(errorMessage = "Ошибка ввода-вывода: ${e.message}")
        } catch (e: HttpException) {
            _todoItemsScreenUiState.value =
                todoItemsScreenUiState.value.copy(lastOperation = VariantFunction.GET)
            _todoItemsScreenUiState.value =
                todoItemsScreenUiState.value.copy(errorMessage = "Ошибка HTTP: ${e.message}")
        } catch (e: UnknownHostException) {
            _todoItemsScreenUiState.value =
                todoItemsScreenUiState.value.copy(lastOperation = VariantFunction.GET)
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
            repository.getTasks().collect { state ->
                when (state) {
                    is DataState.Result -> {
                        _todoItemsScreenUiState.emit(
                            repository.remoteDataFlow.value
                        )
                        //_undoneTasks.emit(UiState.Success(state.data.filter { !it.isDone }))
                    }
                    else -> {}
                }
            }

                    //repository.getTasksFlow().collect {
//            repository.getTasks().collect{
//                _todoItemsScreenUiState.value = it
//            }
        }
        //updateTasks()
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

    private fun getNumberImportance(title: String): Int {
        val number =
            when (title) {
                stringRecurse.getString(Relevance.ORDINARY.textNameForJson()) -> 0
                stringRecurse.getString(Relevance.LOW.textNameForJson()) -> 1
                stringRecurse.getString(Relevance.URGENT.textNameForJson()) -> 2

                else -> -1
            }
        return number
    }

    private fun getTitleImportance(selectedImportance: Int): String {
        val titleImportance =
            when (selectedImportance) {
                -1, 0 -> stringRecurse.getString(Relevance.ORDINARY.textNameForJson())
                1 -> stringRecurse.getString(Relevance.LOW.textNameForJson())
                else -> stringRecurse.getString(Relevance.URGENT.textNameForJson())
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
        item: TodoModel?,
        context: Context
    ) {
        Log.d("SelectItem", item.toString())
        val dataOfNewItem = addTodoUiState.value
        val newItem = TodoModel(
            dataOfNewItem.id,
            dataOfNewItem.description,
            dataOfNewItem.relevance.toString().numberToRelevance(),
            deadline = dataOfNewItem.deadline ?: 0L,
            executionFlag = dataOfNewItem.executionFlag,
            dateOfCreating = if (dataOfNewItem.dateOfCreating == 0L) Calendar.getInstance().timeInMillis
            else
                dataOfNewItem.dateOfCreating,
            dateOfEditing = dataOfNewItem.dateOfEditing ?: Calendar.getInstance().timeInMillis,
        )
        if (item == TodoModel() || item == null) {
            addFlow(newItem)
        } else {
            updateFlow(newItem)
        }
    }

    fun formatDate(date: Long?): String {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return dateFormat.format(Date(date ?: 0L))
    }

    private fun provideSuccess(list: List<TodoModel>, flag: Boolean){
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
    private fun addFlow(item: TodoModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTask(item.text, item.relevance, item.deadline)
        }
    }

    fun removeFlow(item: TodoModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(item)
        }
    }

    private fun updateFlow(item: TodoModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(item)
        }
    }
}