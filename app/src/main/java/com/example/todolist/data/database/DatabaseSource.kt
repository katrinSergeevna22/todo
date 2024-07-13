package com.example.todolist.data.database

import com.example.todolist.data.network.util.toEntity
import com.example.todolist.data.network.util.toModel
import com.example.todolist.domain.IDatabaseSource
import com.example.todolist.domain.model.TodoModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseSource @Inject constructor(
    private val dao: TodoDao
): IDatabaseSource {
    override fun getTasks(): Flow<DatabaseState<List<TodoModel>>> = flow {
        emit(DatabaseState.Initial)
        dao.getTasks().collect { list ->
            list.map(TodoEntity::toModel)
                .sortedByDescending { maxOf(it.dateOfCreating, it.dateOfEditing ?: Long.MIN_VALUE) }
                .also { tasks -> emit(DatabaseState.Success(tasks)) }
        }
    }.catch {
        emit(DatabaseState.Failure(it))
    }

    override fun getTask(uuid: UUID): Flow<DatabaseState<TodoModel>> = flow {
        emit(DatabaseState.Initial)
        dao.getTask(uuid.toString()).collect {
            when (it) {
                null -> emit(DatabaseState.Failure(DatabaseException("Item not found!")))
                else -> emit(DatabaseState.Success(it.toModel()))
            }
        }
    }.catch {
        emit(DatabaseState.Failure(it))
    }

    override suspend fun addTask(task: TodoModel) = dao.addTask(task.toEntity())

    override suspend fun deleteTask(task: TodoModel) = dao.removeTask(task.toEntity())

    override suspend fun getTasksAsList(): List<TodoModel> = dao.getTasksAsEntity().map(TodoEntity::toModel)

    override suspend fun getTaskAsEntity(uuid: UUID): TodoModel? = dao.getTaskAsEntity(uuid.toString())?.toModel()

    override fun overwrite(tasks: List<TodoModel>) {
        dao.removeTasks()
        dao.addTasks(tasks.map(TodoModel::toEntity))
    }

}