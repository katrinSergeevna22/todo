package com.example.todoapp.adapter_todo_item

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.Relevance
import com.example.todoapp.TodoItem
import com.example.todolist.R
import com.example.todolist.databinding.TodoItemBinding

class TodoViewHolder(
    private val binding: TodoItemBinding,
    private val onInfoClicked: (TodoItem) -> Unit,
    private val onDoneClicked: (TodoItem) -> Unit,
    private val onDeleteClicked: (TodoItem) -> Unit

    ) : RecyclerView.ViewHolder(binding.root) {

    private var _todoItem: TodoItem? = null
    private val todoItem: TodoItem
        get() = _todoItem!!

    fun onBind(data: TodoItem) {
        _todoItem = data
        with(binding) {
            root.setOnClickListener {
                onInfoClicked(todoItem)
            }
            tvTextOfTodoItem.text = todoItem.text
            if (todoItem.relevance == Relevance.URGENT.getRelevance()) {
                ibReadlinessFlag.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.ic_readliness_flag_unchecked_high
                    )
                )
                tvTextOfTodoItem.text = "!!" + todoItem.text
            }
            else {
                ibReadlinessFlag.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.ic_readiness_flag_normal
                    )
                )
                if (todoItem.relevance == Relevance.LOW.getRelevance()) {
                    tvTextOfTodoItem.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_arrow_down,
                        0,
                        0,
                        0
                    )
                    tvTextOfTodoItem.text = todoItem.text
                }
            }
            if (todoItem.executionFlag) {
                ibReadlinessFlag.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.ic_readliness_flag_checked
                    )
                )
            }
            if (todoItem.deadline != null){
                tvDateTodoItem.text = todoItem.deadline
            }
            else{
                tvDateTodoItem.visibility = View.GONE
            }

        }

    }

    fun setBackgroundColor(color: Int) {
        binding.root.setBackgroundColor(color)
    }
}