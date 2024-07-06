package com.example.todolist.presentation.ui.view

import androidx.fragment.app.Fragment

class AddTodoItemFragment : Fragment() {
    /*lateinit var binding: FragmentAddTodoItemBinding
    var selectTodoItem: TodoItem? = null
    private var todoItem: TodoItem? = null
    var selectedDateInFragment: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            todoItem = it.getParcelable(ARG_TODO_ITEM)

        }

        parentFragmentManager.setFragmentResultListener("calendar_request_key", this) { _, bundle ->
            val selectedDate = bundle.getString("selected_date")
            selectedDate?.let { date ->
                // Обновляем UI или ViewModel с полученной датой
                selectedDateInFragment = selectedDate
                binding.tvDeadline.text = date
                binding.tvDeadline.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.blue)
                )
            }
        }
    }

    companion object {
        private const val ARG_TODO_ITEM = "todo_item"

        @JvmStatic
        fun newInstance(todoItem: TodoItem? = null) =
            AddTodoItemFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_TODO_ITEM, todoItem)
                }
            }
    }


    private lateinit var viewModel: AddTodoItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddTodoItemBinding.inflate(inflater)
        //viewModel = AddTodoItemViewModel.newInstance()
        viewModel = ViewModelProvider(this).get(AddTodoItemViewModel::class.java)
        //val listViewModel = ViewModelProvider(this).get(ListViewModel::class.java)
val listViewModel = ListViewModel.newInstance()

        binding.apply {

            switchDeadline.setOnCheckedChangeListener { _, isChecked ->
                Log.d("InAddTodoFragmentIf2", tvDeadline.toString())
                if (isChecked) {

                    parentFragmentManager.beginTransaction()
                        .replace(binding.frameLayoutForCalendar.id, CalendarFragment.newInstance())
                        .addToBackStack(null)
                        .commit()

                }
            }


            Log.d("here", todoItem.toString())
            if (todoItem == null){
                tvDelete.setTextColor(ContextCompat.getColor((activity as MainActivity),
                    R.color.disableColor
                ))
                ivDelete.setColorFilter(ContextCompat.getColor((activity as MainActivity),
                    R.color.disableColor
                ),
                    PorterDuff.Mode.MULTIPLY);

                Log.d("here2", todoItem.toString())
            tvSave.setOnClickListener {
                val id = LocalDataStore.list.size
                val description = etMultiLine.text.toString()
                val dateOfCreate =
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                val relevance = spinnerRelevance.selectedItem.toString()
                var deadline: String? = null
                if (selectedDateInFragment != null){
                    deadline = selectedDateInFragment
                    Log.d("deadLine", deadline.toString())
                }
                val newTodoItem = TodoItem(
                    id.toString(),
                    description,
                    relevance,
                    deadline,
                    executionFlag = false,
                    dateOfCreating = dateOfCreate
                )

                Log.d("AddToDoItem3", newTodoItem.toString())
                lifecycleScope.launchWhenStarted {
                    listViewModel.addItem(newTodoItem)
                }
                //(parentFragment as ListFragment).adapter.notifyDataSetChanged()
                hideKeyboard()
                requireActivity().supportFragmentManager.popBackStack()
                //listViewModel.addItem(newTodoItem)

                //parentFragmentManager.popBackStack()
            }
            }
            else{
                selectTodoItem = todoItem
                etMultiLine.setText(selectTodoItem?.text)

                val categoriesArray = resources.getStringArray(R.array.relevanceOfTodo)
                val categoryToSet = selectTodoItem?.relevance

                val categoryIndex = categoriesArray.indexOf(categoryToSet)
                if (categoryIndex != -1) {
                    spinnerRelevance.setSelection(categoryIndex)
                }
                if (selectedDateInFragment != null){
                    binding.tvDeadline.text = selectedDateInFragment
                    binding.tvDeadline.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.blue)
                    )
                }

                tvSave.setOnClickListener {
                    val description = etMultiLine.text.toString()
                    val dateOfEdit =
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                    val relevance = spinnerRelevance.selectedItem.toString()
                    var deadline: String? = null
                    if (selectedDateInFragment != null){
                        deadline = selectedDateInFragment
                        Log.d("deadLine", deadline.toString())
                    }
                    val newTodoItem = TodoItem(
                        selectTodoItem?.id ?: LocalDataStore.list.size.toString(),
                        description,
                        relevance,
                        deadline,
                        executionFlag = false,
                        dateOfCreating = selectTodoItem?.dateOfCreating ?: Date().toString(),
                        dateOfEditing = dateOfEdit
                    )

                    lifecycleScope.launchWhenStarted {
                        listViewModel.editItem(newTodoItem)
                    }
                    parentFragmentManager.popBackStack()
                }
            }

            //listViewModel.selectedItem.observeForever { selectTodoItem ->

                //if (selectTodoItem != null) {
                    //Log.d("InAddTodoFragmentIf1", listViewModel..toString())


/*
                } else {

                    listViewModel.selectedItem.observeForever {
                        Log.d("InAddTodoFragmentElse1", it.toString())


                        val categoriesArray = resources.getStringArray(R.array.relevanceOfTodo)
                        val categoryToSet = selectTodoItem?.relevance

                        val categoryIndex = categoriesArray.indexOf(categoryToSet)
                        if (categoryIndex != -1) {
                            spinnerRelevance.setSelection(categoryIndex)
                        }

                        if (selectTodoItem?.deadline != null) {

                        }
                    }
                    /*
                selectTodoItem = listViewModel.selectedItem
                etMultiLine.setText(selectTodoItem.text)

                val categoriesArray = resources.getStringArray(R.array.relevanceOfTodo)
                val categoryToSet = selectTodoItem.relevance

                val categoryIndex = categoriesArray.indexOf(categoryToSet)
                if (categoryIndex != -1) {
                    spinnerRelevance.setSelection(categoryIndex)
                }

                if (selectTodoItem.deadline != null){

                }

                 */

                    tvSave.setOnClickListener {
                        val description = etMultiLine.text.toString()
                        val dateOfEdit =
                            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                        val relevance = spinnerRelevance.selectedItem.toString()
                        val newTodoItem = TodoItem(
                            "",
                            description,
                            relevance,
                            executionFlag = false,
                            dateOfCreating = selectTodoItem?.dateOfCreating ?: Date().toString(),
                            dateOfEditing = dateOfEdit
                        )
                        //listViewModel.updateItem(newTodoItem)
                        parentFragmentManager.popBackStack()
                    }
                }


                tvDelete.setOnClickListener {
                    if (listViewModel.isAddTodoItem) {
                        tvDelete.setTextColor(resources.getColor(R.color.grey))
                        ivDelete.setColorFilter(
                            ContextCompat.getColor(requireContext(), R.color.grey),
                            PorterDuff.Mode.MULTIPLY
                        );
                    } else {
                        //dataRepository.removeTodoItem(selectTodoItem)
                        parentFragmentManager.popBackStack()
                    }

                }
*/
            tvDelete.setOnClickListener {
                if (todoItem == null) {

                } else {
                    lifecycleScope.launchWhenStarted {
                        listViewModel.removeItem(todoItem ?: TodoItem())
                    }
                    parentFragmentManager.popBackStack()
                }

            }
            ibClose.setOnClickListener {
                    requireActivity().supportFragmentManager.popBackStack()
                    //parentFragmentManager.popBackStack()
                }


            }
        //}
        return binding.root
    }
    private fun hideKeyboard() {
        val imm = getSystemService(requireContext(), InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

     */
}



