package com.example.todolist.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.todolist.R

@SuppressLint("StaticFieldLeak")
object SharedPreferencesProvider {
    lateinit var context: Context
    val sharedPreferences: SharedPreferences by lazy {
        Log.d("context", context.toString())
        context.getSharedPreferences(
            ContextCompat.getString(
                context, R.string.name
            ), Context.MODE_PRIVATE
        )
    }
}