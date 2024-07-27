package com.example.todolist.domain.providers

interface IStringProvider {
    fun getString(stringId: Int): String
}