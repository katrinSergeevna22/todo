package com.example.todolist

import android.content.Context
import androidx.room.Room
import com.example.todolist.data.database.AppDatabase
import com.example.todolist.data.database.TodoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideTodoDao(database: AppDatabase): TodoDao = database.todoDao()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase = Room
        .databaseBuilder(
            context,
            AppDatabase::class.java,
            "task_database"
        ).build()

}