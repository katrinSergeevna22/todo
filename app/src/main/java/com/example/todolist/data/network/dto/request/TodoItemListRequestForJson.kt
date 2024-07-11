package com.example.todolist.data.network.dto.request

import com.google.gson.annotations.SerializedName
import com.example.todolist.data.network.dto.response.TodoItemForJson

/**
 * Data class representing a todo item request for JSON serialization.
 *
 * @property element todo item to be serialized
 */
data class TodoItemListRequestForJson(
    @SerializedName("list") val list: List<TodoItemForJson>
)