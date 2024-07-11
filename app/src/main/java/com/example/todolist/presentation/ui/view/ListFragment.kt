package com.example.todolist.presentation.ui.view

import androidx.fragment.app.Fragment
//import com.example.todolist.presentation.ui.theme.MyAppTheme

class ListFragment : Fragment() {
    /* View
    lateinit var binding: FragmentListBinding
    private val dataRepository = TodoItemsRepository.getInstance()
    var listTodoItemAll = mutableListOf<TodoItem>()
    var listTodoItem = mutableListOf<TodoItem>()
    private var itemListJob: Job? = null
    lateinit var adapter : TodoAdapter
    var flag = true
    companion object {
        fun newInstance() = ListFragment()
    }

    private lateinit var viewModel: ListViewModel
    private lateinit var addViewModel: AddTodoItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater)
        //return inflater.inflate(R.layout.fragment_list, container, false)

        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        addViewModel = ViewModelProvider(this).get(AddTodoItemViewModel::class.java)




        adapter = TodoAdapter({
            //viewModel.isAddTodoItem = false
            //viewModel.selectItem = it
            //Log.d("here1", viewModel.selectItem.toString())
            //Log.d("InListFragmentAdapter1", viewModel.isAddTodoItem.toString())

            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, AddTodoItemFragment.newInstance(it))
                .addToBackStack(null)
                .commit()
        },
            onDeleteClicked = { todoItem ->
                lifecycleScope.launch {
                    viewModel.editExecution(todoItem)
                    adapter.notifyDataSetChanged()
                    //viewModel.editItemParameter(todoItem.id, (!todoItem.executionFlag).toString())
                }
            },
            onDoneClicked = { todoItem ->
                lifecycleScope.launch {
                    viewModel.removeItem(todoItem)
                    adapter.notifyDataSetChanged()
                }

            })

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            private val leftSwipeColor =
                ContextCompat.getColor((activity as MainActivity), R.color.red)
            private val rightSwipeColor =
                ContextCompat.getColor((activity as MainActivity), R.color.green)

            private val iconMargin = 16
            private val iconSize = 80

            private val paint = Paint()
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        // Обработка свайпа влево (удаление)
                        val item = adapter.currentList[position]
                        lifecycleScope.launch {
                            viewModel.removeItem(item)
                            /*
                            viewModel.getListData().value?.let { items ->
                                updateList(items)
                            }

                             */
                            adapter.notifyDataSetChanged()
                        }
                        //adapter.notifyDataSetChanged()
                        //onDoneClicked(item)
                    }

                    ItemTouchHelper.RIGHT -> {
                        // Обработка свайпа вправо (выполнение)
                        val item = adapter.currentList[position]
                        lifecycleScope.launch {
                            viewModel.editExecution(item)
                            /*
                            viewModel.getListData().value?.let { items ->
                                updateList(items)
                            }

                             */
                            adapter.notifyDataSetChanged()
                        }
                        //onDeleteClicked(item)
                    }
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView

                // Рисуем красную область и иконку при свайпе влево
                if (dX < 0) {
                    paint.color = leftSwipeColor
                    val background = RectF(
                        itemView.right.toFloat() + dX,
                        itemView.top.toFloat(),
                        itemView.right.toFloat(),
                        itemView.bottom.toFloat()
                    )
                    c.drawRect(background, paint)

                    val icon = ContextCompat.getDrawable(recyclerView.context, R.drawable.ic_delete)
                    icon?.setTint(ContextCompat.getColor(requireContext(), R.color.white))
                    val iconTop = itemView.top + (itemView.height - iconSize) / 2
                    val iconBottom = iconTop + iconSize
                    val iconLeft = itemView.right - iconMargin - iconSize
                    val iconRight = itemView.right - iconMargin
                    icon?.bounds = RectF(
                        iconLeft.toFloat(),
                        iconTop.toFloat(),
                        iconRight.toFloat(),
                        iconBottom.toFloat()
                    ).toRect()
                    icon?.draw(c)
                }

                // Рисуем зеленую область и иконку при свайпе вправо
                if (dX > 0) {
                    paint.color = rightSwipeColor
                    val background = RectF(
                        itemView.left.toFloat(),
                        itemView.top.toFloat(),
                        dX,
                        itemView.bottom.toFloat()
                    )
                    c.drawRect(background, paint)

                    val icon = ContextCompat.getDrawable(recyclerView.context, R.drawable.ic_check)
                    icon?.setTint(ContextCompat.getColor(requireContext(), R.color.white))

                    val iconTop = itemView.top + (itemView.height - iconSize) / 2
                    val iconBottom = iconTop + iconSize
                    val iconLeft = itemView.left + iconMargin
                    val iconRight = itemView.left + iconMargin + iconSize
                    icon?.bounds = RectF(
                        iconLeft.toFloat(),
                        iconTop.toFloat(),
                        iconRight.toFloat(),
                        iconBottom.toFloat()
                    ).toRect()
                    icon?.draw(c)
                }

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.rcView)
        binding.apply {
            rcView.layoutManager = LinearLayoutManager(activity as MainActivity)
            rcView.adapter = adapter

            /*
            var getItems = viewModel.getListDataFlag()
            viewModel.getListData().observe(viewLifecycleOwner, Observer {list->

                adapter.submitList(list)
                Log.d("flag", flag.toString())
            })

            viewModel.getListData().value?.let { items ->
                updateList(items)
            }


             */

            lifecycleScope.launchWhenStarted {
                viewModel.getListData().collect { items ->
                    adapter.submitList(items)
                }
            }

            val headerObserver = object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val headerHeight = header.height
                    Log.d("InHeaderObserver", headerHeight.toString())
                    if (headerHeight < 300) {
                        tvHeaderLarge.visibility = View.GONE
                        tvHeaderSmall.visibility = View.VISIBLE
                        tvDone.visibility = View.GONE
                        ibVisibilityLarge.visibility = View.GONE
                        ibVisibilitySmall.visibility = View.VISIBLE

                        //viewModel.addItem(TodoItem("", "", "", "", false, ""))
                    }
                    if (headerHeight >= 300) {
                        tvHeaderSmall.visibility = View.GONE
                        tvHeaderLarge.visibility = View.VISIBLE
                        tvDone.visibility = View.VISIBLE
                        ibVisibilitySmall.visibility = View.GONE
                        ibVisibilityLarge.visibility = View.VISIBLE
                    }

                }
            }

            header.viewTreeObserver.addOnGlobalLayoutListener(headerObserver)

            ibVisibilityLarge.setOnClickListener {
                val isVisibilityOnNow = viewModel.isVisibilityOn
                flag = !isVisibilityOnNow
                viewModel.isVisibilityOn = !isVisibilityOnNow
                if (!isVisibilityOnNow){
                    ibVisibilityLarge.setImageDrawable(ContextCompat.getDrawable((activity as MainActivity),
                        R.drawable.ic_visibility
                    ))
                    //getItems = viewModel.getListData()
                    /*
                    viewModel.getListData().value?.let { items ->
                        updateList(items)
                    }
                     */
                //adapter.submitList(adapter.currentList.filter { !it.executionFlag })
                }
                else{
                    //getItems = viewModel.getListDataFlag()
                    ibVisibilityLarge.setImageDrawable(ContextCompat.getDrawable((activity as MainActivity),
                        R.drawable.ic_visibility_off
                    ))
                    /*
                    viewModel.getListData().value?.let { items ->
                        updateList(items)
                    }
                     */
                    //adapter.submitList(listTodoItemAll)
                }

            }

            ibVisibilitySmall.setOnClickListener {
                val isVisibilityOnNow = viewModel.isVisibilityOn
                viewModel.isVisibilityOn = !isVisibilityOnNow
                if (!isVisibilityOnNow){
                    ibVisibilitySmall.setImageDrawable(ContextCompat.getDrawable((activity as MainActivity),
                        R.drawable.ic_visibility
                    ))
                    /*
                    viewModel.getListData().value?.let { items ->
                        updateList(items)
                    }

                     */
                //adapter.submitList(adapter.currentList.filter { !it.executionFlag })
                }
                else{
                    ibVisibilitySmall.setImageDrawable(ContextCompat.getDrawable((activity as MainActivity),
                        R.drawable.ic_visibility_off
                    ))
                    /*
                    viewModel.getListData().value?.let { items ->
                        updateList(items)
                    }
                     */
                //adapter.submitList(listTodoItemAll)
                }

            }

            addButton.setOnClickListener {
                addButton.visibility = View.GONE
                //viewModel.isAddTodoItem = true

                //listTodoItem.add(TodoItem("from list", "", "", "", false, "", ""))
                //Log.d("AddButton", viewModel.isAddTodoItem.toString())

                parentFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, AddTodoItemFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
            }
            adapter.notifyDataSetChanged()
        }
        return binding.root
    }

    fun updateList(list : List<TodoItem>) {

        val filteredItems = if (flag){
            Log.d("InOdserve2", list.toString())
            list.filter { !it.executionFlag }
        }
        else{
            list
        }
        Log.d("InOdserve3", filteredItems.toString())
        adapter.submitList(filteredItems)
        binding.tvDone.text = ContextCompat.getString(
            (activity as MainActivity),
            R.string.done
        ) + " - " + (list.size - list.filter { !it.executionFlag }.size)

    }

     */

}

