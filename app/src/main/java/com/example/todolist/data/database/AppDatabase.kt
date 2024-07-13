package com.example.todolist.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dagger.Binds

@Database(entities = [TodoEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}
