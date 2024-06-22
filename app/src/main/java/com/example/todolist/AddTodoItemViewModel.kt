package com.example.todoapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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

    fun addItem(item: TodoItem) {
        dataRepository.addItem(item)
    }


}