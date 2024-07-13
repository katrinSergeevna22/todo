package com.example.todolist.data.repository

import android.util.Log
import com.example.todolist.data.LocalDataStore
import com.example.todolist.domain.model.TodoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

/**
 * Local repository
 * */
class TodoItemsRepository private constructor() {
    private var _list = MutableStateFlow<List<TodoModel>>(LocalDataStore.list)
    val list = _list.asStateFlow()

    //    suspend fun fetchTodoItemList(){
//        withContext(Dispatchers.IO) {
//            _list.value = LocalDataStore.list
//        }
//    }
    suspend fun getTodoItemList(): Flow<List<TodoModel>> {
        Log.d("NewStateRepo", list.value.toString())
        return list
    }

    suspend fun addTodoItem(item: TodoModel) {
        Log.d("AddCor1", Thread.currentThread().name)
        withContext(Dispatchers.IO) {
            //_list.value += item
            LocalDataStore.list += item
            Log.d("AddCor2", Thread.currentThread().name)
        }

    }

    suspend fun removeTodoItem(item: TodoModel) {
        withContext(Dispatchers.IO) {
            //_list.value -= item
            LocalDataStore.list -= item
        }
    }

    suspend fun editTodoItem(newItem: TodoModel) {
        withContext(Dispatchers.IO) {
            LocalDataStore.list.forEach {
                if (newItem.id == it.id) {
                    it.text = newItem.text
                    it.relevance = newItem.relevance
                    it.deadline = newItem.deadline
                    it.executionFlag = newItem.executionFlag
                    it.dateOfEditing = newItem.dateOfEditing
                    it.deadline = newItem.deadline
                }
            }
        }
    }

    suspend fun achieve(item: TodoModel) {
        withContext(Dispatchers.IO) {
            LocalDataStore.list.forEach {
                if (item.id == it.id) {
                    it.executionFlag = !item.executionFlag
                }
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
/*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListRepository(private val apiService: ApiService) {

    fun getList(token: String, callback: (Result<ListResponse>) -> Unit) {
        apiService.getList("OAuth $token").enqueue(object : Callback<ListResponse> {
            override fun onResponse(call: Call<ListResponse>, response: Response<ListResponse>) {
                if (response.isSuccessful) {
                    callback(Result.success(response.body()!!))
                } else {
                    callback(Result.failure(Exception("Error ${response.code()}")))
                }
            }

            override fun onFailure(call: Call<ListResponse>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    fun updateList(token: String, revision: Int, list: List<ListItem>, callback: (Result<ListResponse>) -> Unit) {
        apiService.updateList("OAuth $token", revision, list).enqueue(object : Callback<ListResponse> {
            override fun onResponse(call: Call<ListResponse>, response: Response<ListResponse>) {
                if (response.isSuccessful) {
                    callback(Result.success(response.body()!!))
                } else {
                    if (response.code() == 400) {
                        callback(Result.failure(Exception("Revision mismatch error")))
                    } else {
                        callback(Result.failure(Exception("Error ${response.code()}")))
                    }
                }
            }
            override fun onFailure(call: Call<ListResponse>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }
/*
    fun addItem(token: String, item: ListItem, callback: (Result<ListResponse>) -> Unit) {
        apiService.addItem("OAuth $token", item).enqueue(object : Callback<ListResponse> {
            override fun onResponse(call: Call<ListResponse>, response: Response<ListResponse>) {
                if (response.isSuccessful) {
                    callback(Result.success(response.body()!!))
                } else {
                    callback(Result.failure(Exception("Error ${response.code()}")))
                }
            }

            override fun onFailure(call: Call<ListResponse>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    fun deleteItem(token: String, id: String, callback: (Result<Void>) -> Unit) {
        apiService.deleteItem("OAuth $token", id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback(Result.success(response.body()))
                } else {
                    if (response.code() == 404) {
                        callback(Result.failure(Exception("Item not found")))
                    } else if (response.code() == 400) {
                        callback(Result.failure(Exception("Revision mismatch error")))
                    } else {
                        callback(Result.failure(Exception("Error ${response.code()}")))
                    }
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }
}
 */