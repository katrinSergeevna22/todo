package com.example.todoapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class TodoItemsRepository  private constructor() {
    private val dataList = mutableListOf<TodoItem>()
    private val dataListLiveData = MutableLiveData<List<TodoItem>>()
    private val dataListLiveDataFlag = MutableLiveData<List<TodoItem>>()

    fun getList(): LiveData<List<TodoItem>> {
        dataListLiveData.value = LocalDataStore.list
        return dataListLiveData
    }
    fun getListFlag():LiveData<List<TodoItem>> {
        dataListLiveData.value = LocalDataStore.listOfFalseFlag
        return dataListLiveDataFlag
    }
    fun addItem(item: TodoItem) {
        LocalDataStore.list.add(item)
        LocalDataStore.listOfFalseFlag.add(item)
        dataList.add(item)
    }
    fun removeItem(item: TodoItem){
        LocalDataStore.list.remove(item)
        LocalDataStore.listOfFalseFlag.remove(item)
    }
    fun editItem(item: TodoItem){
        LocalDataStore.list.forEach {
            if (item.id == it.id){
                it.text = item.text
                it.relevance = item.text
                it.deadline = item.deadline
                it.executionFlag = item.executionFlag
                it.dateOfEditing = item.dateOfEditing
                it.deadline = item.deadline
                Log.d("deadlineInedit",item.deadline.toString())
            }
        }
    }
    fun achieve(item: TodoItem){
        if (item.executionFlag)
            LocalDataStore.listOfFalseFlag.remove(item)
        else
            LocalDataStore.listOfFalseFlag.add(item)

        LocalDataStore.list.forEach {
            if (item.id == it.id) {
                it.executionFlag = !item.executionFlag
            }
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
