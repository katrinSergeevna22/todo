package com.example.todolist.data.network.dto.response

import com.google.gson.annotations.SerializedName
import java.util.UUID

/**
 * Data class representing a todo item for JSON serialization.
 *
 * @property id unique identifier of the todo item
 * @property text text description of the todo item
 * @property importance importance level of the todo item
 * @property deadline deadline for the todo item (nullable)
 * @property done flag indicating if the todo item is done
 * @property color color associated with the todo item (nullable)
 * @property createdAt timestamp of when the todo item was created
 * @property changedAt timestamp of when the todo item was last changed
 * @property deviceID unique identifier of the device that last updated the todo item
 */
data class TodoItemForJson(
    @SerializedName("id") val id: UUID,
    @SerializedName("text") val text: String,
    @SerializedName("importance") val importance: String,
    @SerializedName("deadline") val deadline: Long?,
    @SerializedName("done") val done: Boolean,
    @SerializedName("color") val color: String?,
    @SerializedName("created_at") val createdAt: Long,
    @SerializedName("changed_at") val changedAt: Long,
    @SerializedName("last_updated_by") val deviceID: String
)
