package com.example.todolist.presentation.adapter_todo_item

/* Осталось с реализации на View
class TodoAdapter(
    private val onInfoClicked: (TodoItem) -> Unit,
    private val onDeleteClicked: (TodoItem) -> Unit,
    private val onDoneClicked: (TodoItem) -> Unit
) : ListAdapter<TodoItem, TodoViewHolder>(TodoDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TodoItemBinding.inflate(inflater, parent, false)

        return TodoViewHolder(binding, onInfoClicked, onDeleteClicked, onDoneClicked)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.onBind(getItem(position))

    }


}

 */