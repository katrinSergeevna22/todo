package com.example.todolist.data.network.util

import com.example.todolist.domain.model.TodoItem

import com.example.todolist.data.network.dto.response.TodoItemForJson

fun TodoItem.forJson(login: String): TodoItemForJson = TodoItemForJson(
    id,
    text,
    relevance,
    deadline,
    executionFlag,
    "#FFFFFF",
    dateOfCreating,
    dateOfEditing ?: 0L,
    login
)

fun TodoItemForJson.toModel(): TodoItem = TodoItem(
    id,
    text,
    importance,
    deadline,
    done,
    createdAt,
    changedAt,
)

fun String.toToken(): String = "Bearer $this"
