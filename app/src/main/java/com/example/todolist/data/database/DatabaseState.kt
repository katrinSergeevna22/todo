package com.example.todolist.data.database

sealed class DatabaseState <out T> {
    object Initial : DatabaseState<Nothing>()
    data class Success<T>(val data: T) : DatabaseState<T>()
    data class Failure(val cause: Throwable): DatabaseState<Nothing>()
}