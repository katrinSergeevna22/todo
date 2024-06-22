package com.example.todoapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class ListViewModel : ViewModel() {
    private val dataRepository = TodoItemsRepository.getInstance()
    var isAddTodoItem = true
    var isVisibilityOn = true

    companion object {
        fun newInstance() = ListViewModel()
    }
    init {
        getListData()
    }

    fun getListData(): LiveData<List<TodoItem>> {
        val itemLiveData = dataRepository.getList()
        return itemLiveData
    }
    fun getListDataFlag(): LiveData<List<TodoItem>> {
        val itemLiveData = dataRepository.getListFlag()

        return itemLiveData
    }
    fun addItem(item: TodoItem) {
        dataRepository.addItem(item)
    }
    fun removeItem(item: TodoItem) {
        dataRepository.removeItem(item)
    }
    fun editItem(item: TodoItem) {
        dataRepository.editItem(item)
    }
    fun editExecution(item: TodoItem) {
        dataRepository.achieve(item)
    }

}
