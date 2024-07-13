package com.example.todolist.data.network.util

import android.content.Context
import com.example.todolist.data.database.TodoEntity
import com.example.todolist.domain.model.TodoModel

import com.example.todolist.data.network.dto.response.TodoItemForJson
import com.example.todolist.domain.Relevance
import com.example.todolist.domain.numberToRelevance
import com.example.todolist.domain.textName
import com.example.todolist.domain.textNameForJson
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID

fun TodoEntity.toModel(): TodoModel = TodoModel(
    UUID.fromString(id), text, relevance, deadline, isDone, creationTime, editingTime
)
fun TodoModel.toEntity(): TodoEntity = TodoEntity(
    id.toString(), text, relevance, executionFlag, dateOfCreating, dateOfEditing, deadline
)
fun TodoModel.forJson(login: String): TodoItemForJson = TodoItemForJson(
    id,
    text,
    //context.getString(relevance.textNameForJson()),
    relevance.textName(),
    deadline,
    executionFlag,
    "#FFFFFF",
    dateOfCreating,
    dateOfEditing ?: 0L,
    login
)

fun TodoItemForJson.toModel(): TodoModel = TodoModel(
    id,
    text,
    importance.numberToRelevance(),
    deadline,
    done,
    createdAt,
    changedAt,
)

fun String.toToken(): String = "Bearer $this"
