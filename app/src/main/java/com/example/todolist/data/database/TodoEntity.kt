package com.example.todolist.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todolist.domain.Relevance

@Entity(tableName = "tasks")
data class TodoEntity (
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "relevance") val relevance: Relevance,
    @ColumnInfo(name = "is_done") val isDone: Boolean,
    @ColumnInfo(name = "creation_time") val creationTime: Long,
    @ColumnInfo(name = "modifying_time") val editingTime: Long? = null,
    @ColumnInfo(name = "deadline") val deadline: Long? = null
)