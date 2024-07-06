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
fun Relevance.textName(context: Context) : String{
    return when (this) {
        Relevance.ORDINARY -> context.getString(R.string.ordinary)
        Relevance.LOW -> context.getString(R.string.lowRu)
        Relevance.URGENT -> context.getString(R.string.urgentInArray)
    }
}
fun Relevance.textNameForJson(context: Context) : String {
    return when (this) {
        Relevance.ORDINARY -> context.getString(R.string.basic)
        Relevance.LOW -> context.getString(R.string.low)
        Relevance.URGENT -> context.getString(R.string.important)
    }
}