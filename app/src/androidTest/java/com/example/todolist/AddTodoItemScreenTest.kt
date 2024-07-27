package com.example.todolist

import AddTodoItemScreen
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todolist.presentation.viewModel.AddTodoItemViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class AddTodoItemScreenTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createComposeRule()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun testAddTodoItemScreen() {
        composeRule.setContent {
            val viewModel = hiltViewModel<AddTodoItemViewModel>()
            AddTodoItemScreen(item = null, viewModel = viewModel, onBack = {})
        }

        composeRule.onNodeWithText("todoTextField").assertIsDisplayed()
        composeRule.onNodeWithText("addButton").assertIsEnabled()

        composeRule.onNodeWithText("todoTextField").performTextInput("Test Todo Item")
        composeRule.onNodeWithText("addButton").performClick()
    }
}