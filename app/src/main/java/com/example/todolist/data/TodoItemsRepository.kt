package com.example.todolist.data

import android.util.Log
import com.example.todolist.domain.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext


class TodoItemsRepository  private constructor() {
    private val _listFiltered = MutableStateFlow<List<TodoItem>>(LocalDataStore.listOfFalseFlag)
    val listFiltered = _listFiltered.asStateFlow()

    private val _list = MutableStateFlow<List<TodoItem>>(LocalDataStore.list)
    val list = _list.asStateFlow()

    fun getTodoItemList() : Flow<List<TodoItem>>{
        return list
    }
    fun getFilterTodoItemList() : Flow<List<TodoItem>>{
        return listFiltered
    }
    suspend fun addTodoItem(item: TodoItem){
        Log.d("AddCor1", Thread.currentThread().name)
        withContext(Dispatchers.IO) {
            _list.value += item
            _listFiltered.value += item
            LocalDataStore.list += item
            LocalDataStore.listOfFalseFlag += item
            Log.d("AddCor2", Thread.currentThread().name)
        }
    }
    suspend fun removeTodoItem(item: TodoItem){
        withContext(Dispatchers.IO) {
            _list.value -= item
            _listFiltered.value -= item
            LocalDataStore.list -= item
            LocalDataStore.listOfFalseFlag -= item
        }
    }
    suspend fun editTodoItem(newItem: TodoItem) {
        withContext(Dispatchers.IO) {
            val updatedList = _list.value.toMutableList()
            updatedList.forEach {
                if (newItem.id == it.id) {
                    it.text = newItem.text
                    it.relevance = newItem.relevance
                    it.deadline = newItem.deadline
                    it.executionFlag = newItem.executionFlag
                    it.dateOfEditing = newItem.dateOfEditing
                    it.deadline = newItem.deadline
                }
            }
            _list.value = updatedList
            _listFiltered.value = updatedList.filter { !it.executionFlag }

        }
    }
    suspend fun achieve(item: TodoItem){
        withContext(Dispatchers.IO) {
            val updatedList = _list.value.toMutableList()

            updatedList.forEach {
                if (item.id == it.id) {
                    it.executionFlag = !item.executionFlag
                }
            }
            _list.value = updatedList
            _listFiltered.value = updatedList.filter { !it.executionFlag }
        }
    }

    companion object {
        @Volatile
        private var instance: TodoItemsRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: TodoItemsRepository().also { instance = it }
            }
    }
}

