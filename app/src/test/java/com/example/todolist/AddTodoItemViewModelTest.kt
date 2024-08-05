package com.example.todolist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.todolist.domain.Relevance
import com.example.todolist.domain.model.DataState
import com.example.todolist.domain.model.TodoModel
import com.example.todolist.domain.providers.IStringProvider
import com.example.todolist.domain.repository.ITaskLocalRepository
import com.example.todolist.domain.textNameForJson
import com.example.todolist.presentation.viewModel.AddTodoItemViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.HttpException
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

@ExperimentalCoroutinesApi
class AddTodoItemViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: ITaskLocalRepository

    @Mock
    private lateinit var stringResource: IStringProvider

    private lateinit var viewModel: AddTodoItemViewModel

    private val dispatcher = StandardTestDispatcher()
    private val testCoroutineScope = TestCoroutineScope(dispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        MockitoAnnotations.openMocks(this)
        viewModel = AddTodoItemViewModel(repository, stringResource)
    }

    @After
    fun tearDown() {
        testCoroutineScope.cleanupTestCoroutines()
        Dispatchers.resetMain()
    }

    @Test
    fun `loadInState should populate UI state with TodoModel data`(): Unit = runBlocking {
        val todoItem = TodoModel(
            id = UUID.randomUUID(),
            text = "Test Todo",
            relevance = Relevance.URGENT,
            deadline = System.currentTimeMillis(),
            executionFlag = false,
            dateOfCreating = System.currentTimeMillis(),
            dateOfEditing = System.currentTimeMillis()
        )

        `when`(stringResource.getString(Relevance.URGENT.textNameForJson())).thenReturn("Urgent")

        viewModel.loadInState(todoItem)

        viewModel.getAddTodoUiState().test {
            val uiState = awaitItem()
            assertEquals(todoItem.text, uiState.description)
            assertEquals(
                2,
                uiState.relevance
            )
            assertEquals(todoItem.deadline, uiState.deadline)
            assertEquals(todoItem.executionFlag, uiState.executionFlag)
        }
    }

    @Test
    fun `fetchFlow should collect tasks from repository`(): Unit = runBlocking {
        val mockTaskList = listOf(
            TodoModel(
                id = UUID.randomUUID(),
                text = "Test Todo 1",
                relevance = Relevance.URGENT,
                deadline = System.currentTimeMillis(),
                executionFlag = false,
                dateOfCreating = System.currentTimeMillis(),
                dateOfEditing = System.currentTimeMillis()
            ),
            TodoModel(
                id = UUID.randomUUID(),
                text = "Test Todo 2",
                relevance = Relevance.ORDINARY,
                deadline = System.currentTimeMillis(),
                executionFlag = false,
                dateOfCreating = System.currentTimeMillis(),
                dateOfEditing = System.currentTimeMillis()
            ),
        )
        `when`(repository.getTasks()).thenReturn(flow { emit(DataState.Result(mockTaskList)) })

    }

    @Test
    fun `setText should update description in UI state`() {
        val newText = "New Todo Description"

        viewModel.setText(newText)

        assertEquals(newText, viewModel.getAddTodoUiState().value.description)
    }

    @Test
    fun `setImportant should update relevance in UI state`() {
        val importanceIndex = 1

        viewModel.setImportant(importanceIndex)

        assertEquals(importanceIndex, viewModel.getAddTodoUiState().value.relevance)
    }

    @Test
    fun `setDeadline should update deadline in UI state`() {
        val newDeadline: Long? = System.currentTimeMillis()

        viewModel.setDeadline(newDeadline)

        assertEquals(newDeadline, viewModel.getAddTodoUiState().value.deadline)
    }

    @Test
    fun `textIsNotEmpty should return true when description is not empty`() {
        viewModel.setText("Some description")
        assertEquals(true, viewModel.textIsNotEmpty())
    }

    @Test
    fun `textIsNotEmpty should return false when description is empty`() {
        viewModel.setText("")
        assertEquals(false, viewModel.textIsNotEmpty())
    }

    @Test
    fun `saveItemByButton should add new item when item is null`(): Unit = runBlocking {
        val newItem =
            TodoModel(
                id = UUID.randomUUID(),
                text = "Test Todo",
                relevance = Relevance.ORDINARY,
                executionFlag = false,
                dateOfCreating = System.currentTimeMillis(),
                dateOfEditing = System.currentTimeMillis()
            )

        viewModel.setText(newItem.text)
        viewModel.saveItemByButton(null)

        verify(repository).addTask(newItem.text, newItem.relevance, newItem.deadline)
    }

    @Test
    fun `saveItemByButton should update existing item when item is not null`() = runBlocking {
        val existingItem =
            TodoModel(
                id = UUID.randomUUID(),
                text = "Test Todo",
                relevance = Relevance.ORDINARY,
                executionFlag = false,
                dateOfCreating = System.currentTimeMillis(),
                dateOfEditing = System.currentTimeMillis()
            )

        val updatedItem = existingItem.copy(text = "Updated Todo")

        viewModel.setText(updatedItem.text)
        viewModel.saveItemByButton(existingItem)

        verify(repository).updateTask(updatedItem)
    }

    @Test
    fun `formatDate should return formatted date string`() {
        val dateInMillis = Calendar.getInstance().timeInMillis

        val formattedDate = viewModel.formatDate(dateInMillis)

        val expectedDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        assertEquals(expectedDateFormat.format(Date(dateInMillis)), formattedDate)
    }

}