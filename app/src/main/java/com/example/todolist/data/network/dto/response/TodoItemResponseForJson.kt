package com.example.todolist.data.network.dto.response

import com.google.gson.annotations.SerializedName

/**
 * Data class representing a todo item response for JSON serialization.
 *
 * @property status status message of the response
 * @property element todo item in the response
 * @property revision revision number of the response
 */
data class TodoItemResponseForJson(
    @SerializedName("status") val status: String,
    @SerializedName("element") val element: TodoItemForJson,
    @SerializedName("revision") val revision: Int
)
