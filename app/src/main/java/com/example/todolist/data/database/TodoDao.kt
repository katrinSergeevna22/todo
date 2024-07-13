package com.example.todolist.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTask(id: String): Flow<TodoEntity?>

    @Insert(entity = TodoEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask(task: TodoEntity)

    @Delete(entity = TodoEntity::class)
    suspend fun removeTask(task: TodoEntity)

    @Query("SELECT * FROM tasks")
    fun getTasks(): Flow<List<TodoEntity>>

    @Insert(entity = TodoEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun addTasks(tasks: List<TodoEntity>)

    @Query("DELETE FROM tasks")
    fun removeTasks()

    @Query("SELECT * FROM tasks")
    fun getTasksAsEntity(): List<TodoEntity>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskAsEntity(id: String): TodoEntity?
}