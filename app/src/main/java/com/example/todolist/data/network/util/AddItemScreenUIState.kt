package com.example.todolist.data.network.util

import com.example.todolist.domain.Relevance
import java.util.UUID

data class AddItemScreenUIState (
    val id: UUID = UUID.randomUUID(),
    val description: String = "",
    val relevance: Int = Relevance.ORDINARY.ordinal,
    val deadline: Long? = null,
    val executionFlag: Boolean = false,
    val dateOfCreating: Long = 0L,
    val dateOfEditing: Long? = null,
)