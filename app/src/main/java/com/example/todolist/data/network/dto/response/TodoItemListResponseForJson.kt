package com.example.todolist.data.network.dto.response

import com.example.todolist.data.network.dto.response.TodoItemForJson
import com.google.gson.annotations.SerializedName

/**
 * Data class representing a todo item list response for JSON serialization.
 *
 * @property status status message of the response
 * @property list list of todo items in the response
 * @property revision revision number of the response
 */
data class TodoItemListResponseForJson(
    @SerializedName("status") val status: String,
    @SerializedName("list") val list: List<TodoItemForJson>,
    @SerializedName("revision") val revision: Int
)
