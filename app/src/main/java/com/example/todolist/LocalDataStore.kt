package com.example.todoapp

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object LocalDataStore {
    val list = mutableListOf<TodoItem>()
    val listOfFalseFlag = mutableListOf<TodoItem>()
    init {
        val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        list.add(TodoItem("1", "Купить пальто", Relevance.ORDINARY.getRelevance(), null,false, date, null))
        list.add(TodoItem("2", "Сходить к стоматологу", Relevance.URGENT.getRelevance(), null,false, date, null))
        list.add(TodoItem("3", "Купить подарок дяде на день рождения, в подарок он хочет просто деньки в красивом конверте", Relevance.ORDINARY.getRelevance(), "1 January 2025",false, date, null))
        list.add(TodoItem("4", "Нужно начать ходить в спортзал, для начала купить себе абонимент, попробовать походить на занятия, если понадобится, то можно будет задуматься о тренере", Relevance.ORDINARY.getRelevance(), null,false, date, null))
        list.add(TodoItem("5", "Сходить в недавно открывшееся азиатское кафе", Relevance.LOW.getRelevance(), null,false, date, null))
        list.add(TodoItem("6", "Отвезти собаку на груминг", Relevance.ORDINARY.getRelevance(), "23 July 2024",false, date, null))
        list.add(TodoItem("7", "Купить что-то интересное по подарочному сертификату, а то он скоро истечет, а друзья так старались меня порадовать", Relevance.ORDINARY.getRelevance(), null,false, date, null))
        list.add(TodoItem("8", "Составить расписание ближайших дней, чтобы ничего не забыть", Relevance.LOW.getRelevance(), null,false, date, null))
        list.add(TodoItem("9", "Сходить с сестрой в кино", Relevance.URGENT.getRelevance(), "30 June 2024",false, date, null))
        list.add(TodoItem("10", "Продать ненужный скейт", Relevance.ORDINARY.getRelevance(), null,false, date, null))
        list.add(TodoItem("11", "Посмотреть головоломку 2", Relevance.ORDINARY.getRelevance(), null,false, date, null))
        list.add(TodoItem("12", "Заказать вентилятор", Relevance.ORDINARY.getRelevance(), null,false, date, null))
        list.add(TodoItem("13", "Почитать про дресировку собак", Relevance.LOW.getRelevance(), null,false, date, null))
    }
}
    /*
    private val _list = MutableStateFlow<List<TodoItem>>(emptyList())
    override fun getList(): Flow<List<TodoItem>> = _list.asStateFlow()

    init {
        createTodoItems()
    }
    private fun createTodoItems(){
        val newList = mutableListOf<TodoItem>()
        val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

        newList.add(TodoItem("1", "Купить что-то", Relevance.ORDINARY.getRelevance(), null,false, date, null))
        newList.add(TodoItem("2", "Купить что-то", Relevance.ORDINARY.getRelevance(), null,false, date, null))
        newList.add(TodoItem("3", "Купить что-то, где-то, зачем-то, но зачем не очень понятно", Relevance.ORDINARY.getRelevance(), "1 января 2025",false, date, null))
        newList.add(TodoItem("4", "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обрезается текст", Relevance.ORDINARY.getRelevance(), null,false, date, null))
        newList.add(TodoItem("5", "Купить что-то", Relevance.LOW.getRelevance(), null,false, date, null))
        newList.add(TodoItem("6", "Купить что-то", Relevance.URGENT.getRelevance(), null,false, date, null))
        newList.add(TodoItem("1", "Купить что-то", Relevance.ORDINARY.getRelevance(), null,false, date, null))
        newList.add(TodoItem("2", "Купить что-то", Relevance.ORDINARY.getRelevance(), null,false, date, null))
        newList.add(TodoItem("3", "Купить что-то, где-то, зачем-то, но зачем не очень понятно", Relevance.ORDINARY.getRelevance(), null,false, date, null))
        newList.add(TodoItem("4", "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обрезается текст, что-то на экране он плохо образается", Relevance.ORDINARY.getRelevance(), null,false, date, null))
        newList.add(TodoItem("5", "Купить что-то", Relevance.LOW.getRelevance(), null,false, date, null))
        newList.add(TodoItem("6", "Купить что-то", Relevance.URGENT.getRelevance(), null,false, date, null))
        newList.add(TodoItem("1", "Купить что-то", Relevance.ORDINARY.getRelevance(), null,false, date, null))
        newList.add(TodoItem("2", "Купить что-то", Relevance.ORDINARY.getRelevance(), null,false, date, null))
        newList.add(TodoItem("3", "Купить что-то, где-то, зачем-то, но зачем не очень понятно", Relevance.ORDINARY.getRelevance(), null,false, date, null))
        newList.add(TodoItem("4", "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обрезается текст", Relevance.ORDINARY.getRelevance(), null,false, date, null))
        newList.add(TodoItem("5", "Купить что-то", Relevance.LOW.getRelevance(), null,false, date, null))
        newList.add(TodoItem("6", "Купить что-то", Relevance.URGENT.getRelevance(), null,false, date, null))

        _list.value = newList
    }
    override suspend fun addItem(item: TodoItem) {
        _list.value = _list.value + item
        Log.d("FlowLocalDataStore", _list.value.toString())
    }

    override suspend fun editItem(index: String, newItem: TodoItem) {
        val updatedList = _list.value.toMutableList()
        //updatedList[index] = newItem
        _list.value = updatedList
    }

    override suspend fun editItemParameter(index: String, newParameter: String) {
        val updatedList = _list.value.toMutableList()
        //updatedList.filter {it.id == index} = updatedList.filter { it.id == index }.copy(newParameter)
        _list.value = updatedList
    }

    override suspend fun removeItem(item: TodoItem) {
        _list.value = _list.value - item
    }
}

     */