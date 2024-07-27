package com.example.todolist.data.repository

import com.example.todolist.R
import com.example.todolist.data.database.DatabaseState
import com.example.todolist.data.network.util.NetworkException
import com.example.todolist.data.network.util.TodoItemScreenUiState
import com.example.todolist.domain.IDatabaseSource
import com.example.todolist.domain.Relevance
import com.example.todolist.domain.VariantFunction
import com.example.todolist.domain.model.DataState
import com.example.todolist.domain.model.TodoModel
import com.example.todolist.domain.repository.ITaskLocalRepository
import com.example.todolist.domain.repository.ITaskRepository
import com.example.todolist.domain.providers.IStringProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import retrofit2.HttpException
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

class RemoteRepository @Inject constructor(
    private val databaseSource: IDatabaseSource,
    private val remoteRepository: ITaskRepository,
    private val stringProvider: IStringProvider,
) : ITaskLocalRepository {
    private val _remoteDataFlow = MutableStateFlow(TodoItemScreenUiState())
    override val remoteDataFlow = _remoteDataFlow.asStateFlow()

    override fun getTasks(): Flow<DataState<List<TodoModel>>> = flow {
        databaseSource.getTasks().collect { state ->
            when (state) {
                is DatabaseState.Initial -> {
                    DataState.Initial
                    _remoteDataFlow.update {
                        it.copy(loading = true)
                    }
                }

                is DatabaseState.Success -> {
                    _remoteDataFlow.update {
                        it.copy(
                            loading = false,
                            allTodoItemsList = state.data
                        )
                    }
                    emit(DataState.Result(state.data))
                }

                is DatabaseState.Failure -> {
                    _remoteDataFlow.update {
                        it.copy(
                            loading = false,
                            errorMessage = state.cause.message,
                            lastOperation = VariantFunction.GET
                        )
                    }
                    emit(DataState.Exception(state.cause))
                }
            }
        }
    }

    suspend fun synchronization() {
        try {
            _remoteDataFlow.update {
                it.copy(loading = true)
            }
            remoteRepository.synchronize()
            _remoteDataFlow.update {
                it.copy(
                    loading = false
                )
            }
        } catch (e: NetworkException) {
            _remoteDataFlow.update {
                it.copy(
                    loading = false,
                    errorMessage = stringProvider.getString(R.string.errorSynchronized),
                    lastOperation = VariantFunction.UPDATE
                )
            }
        } catch (e: HttpException) {
            _remoteDataFlow.update {
                it.copy(
                    loading = false,
                    errorMessage = stringProvider.getString(R.string.errorSynchronized),
                    lastOperation = VariantFunction.UPDATE
                )
            }
        }
    }

    override suspend fun addTask(text: String, relevance: Relevance, deadline: Long?) {
        val task = buildTodoModel(text, relevance, deadline)
        try {
            databaseSource.addTask(task)
            remoteRepository.addTaskFlow(task)
        } catch (e: IOException) {
            _remoteDataFlow.update {
                it.copy(
                    errorMessage = stringProvider.getString(R.string.errorAddData),
                    lastOperation = VariantFunction.ADD,
                    lastItem = task
                )
            }
        } catch (e: HttpException) {
            _remoteDataFlow.update {
                it.copy(
                    errorMessage = stringProvider.getString(R.string.errorAddData),
                    lastOperation = VariantFunction.ADD,
                    lastItem = task
                )
            }
        }
    }

    override suspend fun deleteTask(task: TodoModel) {
        try {
            databaseSource.deleteTask(task)
            remoteRepository.removeTaskFlow(task)
        } catch (e: IOException) {
            _remoteDataFlow.update {
                it.copy(
                    errorMessage = stringProvider.getString(R.string.errorRemoveData),
                    lastOperation = VariantFunction.DELETE,
                    lastItem = task
                )
            }
        } catch (e: HttpException) {
            _remoteDataFlow.update {
                it.copy(
                    errorMessage = stringProvider.getString(R.string.errorRemoveData),
                    lastOperation = VariantFunction.DELETE,
                    lastItem = task
                )
            }
        }
    }

    override suspend fun updateTask(task: TodoModel) {
        try {
            databaseSource.addTask(task.copy(dateOfEditing = System.currentTimeMillis()))
            remoteRepository.editTaskFlow(task)

        } catch (e: IOException) {
            _remoteDataFlow.update {
                it.copy(
                    errorMessage = stringProvider.getString(R.string.errorEditData),
                    lastOperation = VariantFunction.EDIT,
                    lastItem = task
                )
            }
        } catch (e: HttpException) {
            _remoteDataFlow.update {
                it.copy(
                    errorMessage = stringProvider.getString(R.string.errorEditData),
                    lastOperation = VariantFunction.EDIT,
                    lastItem = task
                )
            }
        }
    }
    fun clearErrorInDataFlow() {
        _remoteDataFlow.value = remoteDataFlow.value.copy(errorMessage = null)
        _remoteDataFlow.value =
            remoteDataFlow.value.copy(lastOperation = null)
        _remoteDataFlow.value =
            remoteDataFlow.value.copy(lastItem = null)
    }
    private fun getUUID(): UUID = UUID.randomUUID()

    private fun buildTodoModel(
        text: String, relevance: Relevance, deadline: Long?
    ): TodoModel = TodoModel(
        getUUID(),
        text,
        relevance,
        deadline,
        false,
        System.currentTimeMillis(),
        System.currentTimeMillis(),
    )
} 