package com.example.todoapp

enum class Relevance {
    LOW {
        override fun getRelevance() : String{
            return "Низкий"
        }
    },
    ORDINARY {
        override fun getRelevance() : String{
            return "Нет"
        }
    },
    URGENT {
        override fun getRelevance() : String{
            return "!! Высокий"
        }
    };
    abstract fun getRelevance() : String
}