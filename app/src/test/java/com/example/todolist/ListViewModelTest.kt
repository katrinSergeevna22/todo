package com.example.todolist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.todolist.data.repository.RemoteRepository
import com.example.todolist.domain.Relevance
import com.example.todolist.domain.VariantFunction
import com.example.todolist.domain.errorHandling.ErrorHandlingImpl
import com.example.todolist.domain.model.DataState
import com.example.todolist.domain.model.TodoModel
import com.example.todolist.domain.providers.IStringProvider
import com.example.todolist.domain.repository.ISettingRepository
import com.example.todolist.presentation.viewModel.ListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import java.util.UUID

@ExperimentalCoroutinesApi
class ListViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ListViewModel

    private val repository: RemoteRepository = mock(RemoteRepository::class.java)
    private val settingParameters: ISettingRepository = mock(ISettingRepository::class.java)
    private val errorHandlingImpl: ErrorHandlingImpl = mock(ErrorHandlingImpl::class.java)
    private val stringProvider: IStringProvider = mock(IStringProvider::class.java)

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = ListViewModel(repository, settingParameters, errorHandlingImpl, stringProvider)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchState should update UI state with fetched tasks`() = runTest {
        val todoItems = listOf(
            TodoModel(
                id = UUID.randomUUID(),
                text = "Test Todo",
                relevance = Relevance.URGENT,
                deadline = System.currentTimeMillis(),
                executionFlag = false,
                dateOfCreating = System.currentTimeMillis(),
                dateOfEditing = System.currentTimeMillis()
            )
        )
        `when`(repository.getTasks()).thenReturn(flowOf(DataState.Result(todoItems)))

        viewModel.fetchState()

        assertEquals(viewModel.getTodoItemsScreenUiState().value.currentTodoItemList, todoItems)
    }

    @Test
    fun `updateTasks should call synchronization if notifications are enabled`() = runTest {
        `when`(settingParameters.getNotificationEnabled()).thenReturn(true)

        viewModel.updateTasks()
        verify(repository).synchronization()
    }

    @Test
    fun `updateTasks should show error if notifications are disabled`() = runTest {
        `when`(settingParameters.getNotificationEnabled()).thenReturn(false)
        `when`(stringProvider.getString(R.string.errorNoInternet)).thenReturn("No internet")

        viewModel.updateTasks()
        verify(errorHandlingImpl).showException("No internet")
    }
}