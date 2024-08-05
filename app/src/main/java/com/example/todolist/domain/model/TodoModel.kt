package com.example.todolist.domain.model

import com.example.todolist.domain.Relevance
import java.util.UUID

/**
 * Data class representing a todo item.
 *
 * @property id unique identifier of the todo item
 * @property text text description of the todo item
 * @property relevance relevance level of the todo item
 * @property deadline deadline for the todo item (nullable)
 * @property executionFlag flag indicating if the todo item is executed
 * @property dateOfCreating timestamp of when the todo item was created
 * @property dateOfEditing timestamp of when the todo item was last edited (nullable)
 */
data class TodoModel(
    val id: UUID,
    var text: String,
    var relevance: Relevance,
    var deadline: Long? = 0L,
    var executionFlag: Boolean,
    val dateOfCreating: Long,
    var dateOfEditing: Long? = null,
)
{
    constructor() : this(UUID.randomUUID(), "", Relevance.ORDINARY, null, false, 0L)

}