package com.example.todolist.domain


import android.content.Context
import com.example.todolist.R

/**
 * Enum class representing the relevance levels of a todo item.
 */
enum class Relevance {
    ORDINARY,
    LOW,
    URGENT
}

fun Relevance.textName(): String {
    return when (this) {
        Relevance.ORDINARY -> "basic"
        Relevance.LOW -> "low"
        Relevance.URGENT -> "important"
    }
}

fun Relevance.textNameForJson(): Int {
    return when (this) {
        Relevance.ORDINARY -> R.string.basic
        Relevance.LOW -> R.string.low
        Relevance.URGENT -> R.string.important
    }
}

fun String.numberToRelevance(): Relevance {
    return when (this) {
        "0" -> Relevance.ORDINARY
        "1" -> Relevance.LOW
        "2" -> Relevance.URGENT
        else -> Relevance.ORDINARY
    }
}