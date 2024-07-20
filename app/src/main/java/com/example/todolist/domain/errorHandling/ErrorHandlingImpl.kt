package com.example.todolist.domain.errorHandling

import android.content.Context
import android.util.Log
import android.widget.Toast
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ErrorHandlingImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : IErrorHandling {
    override fun showException(message: String) {
        try {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        } catch (e: Exception){
            Log.d("Exception", e.toString())
        }

    }
}