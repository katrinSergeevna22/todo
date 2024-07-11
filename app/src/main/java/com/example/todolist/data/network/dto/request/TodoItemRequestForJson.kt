package com.example.todolist.data.network.dto.request

import com.google.gson.annotations.SerializedName
import com.example.todolist.data.network.dto.response.TodoItemForJson

/**
 * Data class representing a todo item list request for JSON serialization.
 *
 * @property list list of todo items to be serialized
 */
data class TodoItemRequestForJson(
    @SerializedName("element") val element: TodoItemForJson
)