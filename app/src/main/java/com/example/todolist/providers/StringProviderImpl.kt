package com.example.todolist.providers

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.todolist.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class StringProviderImpl @Inject constructor(@ApplicationContext private val context: Context) :
    IStringProvider {
    override fun getString(stringId: Int): String {
        return context.getString(R.string.low)
    }
}
