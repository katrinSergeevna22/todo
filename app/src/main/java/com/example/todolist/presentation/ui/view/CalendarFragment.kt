package com.example.todolist.presentation.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.databinding.FragmentCalendarBinding
import com.example.todolist.presentation.viewModel.AddTodoItemViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalendarFragment : Fragment() {
    lateinit var binding: FragmentCalendarBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(inflater)
        val viewModel = ViewModelProvider(requireActivity()).get(AddTodoItemViewModel::class.java)
        binding.apply {
            // Установка сегодняшней даты
            val today = Calendar.getInstance()
            calendarView.date = today.timeInMillis

            btnCancel.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }
            var formattedDate = ""
            // Устанавливаем слушатель на изменение даты
            calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                // Сохранение выбранной даты в переменную
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)

                // Форматирование даты
                val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                formattedDate = dateFormat.format(selectedDate.time)


            }

            btnDone.setOnClickListener {
                // Действие по кнопке Done можно оставить пустым или добавить дополнительную логику
                val bundle = Bundle().apply {
                    putString("selected_date", formattedDate)
                }
                parentFragmentManager.setFragmentResult("calendar_request_key", bundle)

                // Закрываем текущий фрагмент (CalendarFragment)
                requireActivity().supportFragmentManager.popBackStack()
            }

        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = CalendarFragment()
    }
}