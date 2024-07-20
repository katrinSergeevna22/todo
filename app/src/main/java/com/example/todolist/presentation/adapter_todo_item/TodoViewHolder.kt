package com.example.todolist.presentation.adapter_todo_item

/* Осталось с реализации на View
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
            if (todoItem.relevance == Relevance.URGENT.textName(itemView.context)) {
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
                if (todoItem.relevance == Relevance.LOW.textName(itemView.context)) {
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
                tvDateTodoItem.text = todoItem.deadline.toString()
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

 */