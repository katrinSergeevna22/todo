package com.example.todolist.presentation.adapter_todo_item

import androidx.recyclerview.widget.DiffUtil
import com.example.todolist.domain.TodoItem

class TodoDiffUtilCallback : DiffUtil.ItemCallback<TodoItem>() {
    override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean =
        oldItem == newItem
}