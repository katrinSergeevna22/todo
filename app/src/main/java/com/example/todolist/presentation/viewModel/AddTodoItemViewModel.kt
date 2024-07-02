package com.example.todolist.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolist.data.TodoItemsRepository
import com.example.todolist.domain.TodoItem

class AddTodoItemViewModel : ViewModel() {
    companion object {
        fun newInstance() = AddTodoItemViewModel()
    }
    var selectedDate = ""
    var selectedDateLiveData = MutableLiveData<String>()
    fun setSelectedDateLiveData(date: String) {
        selectedDateLiveData.value = date
    }

    var newTodoItem = MutableLiveData<TodoItem>()
    fun setNewTodoItem(item: TodoItem) {
        newTodoItem.value = item
    }


    private val dataRepository = TodoItemsRepository.getInstance()
    val viewModel = ListViewModel()

    suspend fun addItem(item: TodoItem) {
        dataRepository.addTodoItem(item)
    }


}