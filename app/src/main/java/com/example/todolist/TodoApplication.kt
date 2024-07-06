package com.example.todolist

import android.app.Application
import com.example.todolist.data.repository.SharedPreferencesProvider
import com.example.todolist.data.repository.TaskRepository
import com.example.todolist.presentation.viewModel.ListViewModelProvider

class TodoApplication: Application() {
    override fun onCreate() {
        SharedPreferencesProvider.context = this
        ListViewModelProvider.repository = TaskRepository()
        super.onCreate()
    }
}