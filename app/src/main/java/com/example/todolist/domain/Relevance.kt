package com.example.todolist.domain

enum class Relevance {
    ORDINARY {
        override fun getRelevance() : String{
            return "Нет"
        }
    },
    LOW {
        override fun getRelevance() : String{
            return "Низкий"
        }
    },
    URGENT {
        override fun getRelevance() : String{
            return "!! Высокий"
        }
    };
    abstract fun getRelevance() : String
}