package com.example.todolist.data

import com.example.todolist.domain.Relevance
import com.example.todolist.domain.model.TodoItem
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Calendar
import java.util.UUID

object LocalDataStore {
    val list = mutableListOf<TodoItem>()
    val stateFlow = MutableStateFlow<List<TodoItem>>(list)

    init {
        //val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val date = Calendar.getInstance().timeInMillis
        //val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        /*
        list.add(TodoItem(UUID.randomUUID(), "Купить пальто", Relevance.ORDINARY.getRelevance(), null,false, date, null))
        list.add(TodoItem(UUID.randomUUID(),"Сходить к стоматологу", Relevance.URGENT.getRelevance(), null,false, date, null))
        list.add(TodoItem(UUID.randomUUID(),"Купить подарок дяде на день рождения, в подарок он хочет просто деньги в красивом конверте", Relevance.ORDINARY.getRelevance(), date,false, date, null))
        list.add(TodoItem(UUID.randomUUID(),"Нужно начать ходить в спортзал, для начала купить себе абонимент, попробовать походить на занятия, если понадобится, то можно будет задуматься о тренере", Relevance.ORDINARY.getRelevance(), null,false, date, null))
        list.add(TodoItem(UUID.randomUUID(),"Сходить в недавно открывшееся азиатское кафе", Relevance.LOW.getRelevance(), null,false, date, null))
        list.add(TodoItem(UUID.randomUUID(),"Отвезти собаку на груминг", Relevance.ORDINARY.getRelevance(), date,false, date, null))
        list.add(TodoItem(UUID.randomUUID(),"Купить что-то интересное по подарочному сертификату, а то он скоро истечет, а друзья так старались меня порадовать", Relevance.ORDINARY.getRelevance(), null,false, date, null))
        list.add(TodoItem(UUID.randomUUID(),"Составить расписание ближайших дней, чтобы ничего не забыть", Relevance.LOW.getRelevance(), null,false, date, null))
        list.add(TodoItem(UUID.randomUUID(),"Сходить с сестрой в кино", Relevance.URGENT.getRelevance(), date,false, date, null))
        list.add(TodoItem(UUID.randomUUID(), "Продать ненужный скейт", Relevance.ORDINARY.getRelevance(), null,false, date, null))
        list.add(TodoItem(UUID.randomUUID(), "Посмотреть головоломку 2", Relevance.ORDINARY.getRelevance(), null,false, date, null))
        list.add(TodoItem(UUID.randomUUID(), "Заказать вентилятор", Relevance.ORDINARY.getRelevance(), null,false, date, null))
        list.add(TodoItem(UUID.randomUUID(), "Почитать про дресировку собак", Relevance.LOW.getRelevance(), null,false, date, null))

         */

    }
    fun addTodoItem(item: TodoItem){
        list += item
    }
    fun removeTodoItem(item: TodoItem){
        list -= item
    }
    fun editTodoItem(newItem: TodoItem){
        list.forEach {
            if (newItem.id == it.id) {
                it.text = newItem.text
                it.relevance = newItem.relevance
                it.deadline = newItem.deadline
                it.executionFlag = newItem.executionFlag
                it.dateOfEditing = newItem.dateOfEditing
                it.deadline = newItem.deadline
            }
        }
    }
}
