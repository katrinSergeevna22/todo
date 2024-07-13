package com.example.todolist.errorHandling

import android.content.Context
import android.widget.Toast
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ErrorHandlingImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : IErrorHandling {
    override fun showException(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}