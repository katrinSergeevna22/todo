package com.example.todolist.presentation.viewModel

import com.example.todolist.data.repository.TaskRepository

object ListViewModelProvider {
    lateinit var repository: TaskRepository
    val listViewModel: ListViewModel by lazy { ListViewModel(repository) }
}