package com.example.todolist.domain.repository

interface ISynchronized {
    suspend fun synchronize()
}