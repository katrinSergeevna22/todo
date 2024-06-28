package com.example.todolist.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.TodoItemsRepository
import com.example.todolist.domain.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ListViewModel : ViewModel() {
    private val _todoList = MutableStateFlow<List<TodoItem>>(emptyList())
    val todoList: StateFlow<List<TodoItem>> = _todoList


    private val dataRepository = TodoItemsRepository.getInstance()
    val newTodoList: StateFlow<List<TodoItem>> = dataRepository.getTodoItemList()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val newTodoListFiltered: StateFlow<List<TodoItem>> = dataRepository.getFilterTodoItemList()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    //var isAddTodoItem = true
    //var isVisibilityOn = true
    var size = todoList.value.count { it.executionFlag }
    private val _completedCount = MutableStateFlow(0)
    val completedCount: StateFlow<Int> get() = _completedCount

    private val _isVisibilityOnFlow = MutableStateFlow(false) //true)
    val isVisibilityOnFlow: StateFlow<Boolean> = _isVisibilityOnFlow

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage
    private var job: Job? = null

    companion object {
        fun newInstance() = ListViewModel()
    }
    init {
        //getListData()
        //getTodoList()
        fetchTodoList()
        fetchCompletedCount()
    }

    fun getItemById(id: String): TodoItem? {
        return todoList.value.find { it.id == id }
    }

    fun getTodoList() : Flow<List<TodoItem>> {
        return if (_isVisibilityOnFlow.value) {
            newTodoList
        } else {
            newTodoListFiltered
        }
    }

    fun switchVisibility() {
        Log.d("VisSwitch1", isVisibilityOnFlow.value.toString())
        _isVisibilityOnFlow.value = !_isVisibilityOnFlow.value
        fetchTodoList()
    }

    private fun fetchTodoList() {
        job?.cancel()
        job = viewModelScope.launch {
            try {
                //getListData().collect { items ->
                getTodoList().collect { items ->
                    _todoList.value = items
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
        Log.d("VisFetch1", todoList.value.toString())
    }
    private fun fetchCompletedCount() {
        viewModelScope.launch {
            dataRepository.getTodoItemList().collect { items ->
                _completedCount.value = items.count { it.executionFlag }
            }
        }
    }
    fun addItem(item: TodoItem) {
        Log.d("AddCorView1", Thread.currentThread().name)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dataRepository.addTodoItem(item)
                Log.d("AddCorView2", Thread.currentThread().name)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
        //dataRepository.addTodoItem(item)
    }

    fun removeItem(item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dataRepository.removeTodoItem(item)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
        //viewModelScope.launch { dataRepository.removeTodoItem(item) }
    }

    fun editItem(item: TodoItem) {
        // viewModelScope.launch { dataRepository.editTodoItem(item)}
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dataRepository.editTodoItem(item)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun editExecution(item: TodoItem) {
        //viewModelScope.launch {dataRepository.achieve(item)}
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dataRepository.achieve(item)
                fetchCompletedCount()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}