package com.example.todolist.presentation.ui.compose.todo_list

import AddTodoItemScreen
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todolist.R
import com.example.todolist.presentation.ui.theme.Colors
import com.example.todolist.presentation.viewModel.ListViewModel
import java.util.UUID

@SuppressLint("StaticFieldLeak")

//val todoList by viewModel.newTodoList.collectAsState(coroutineContext)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
    viewModel: ListViewModel,
    navToAdd: (UUID?) -> Unit,
    navToSetting: () -> Unit,
    navToAboutApp: () -> Unit
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    val todoItemScreenUiState = viewModel.getTodoItemsScreenUiState().collectAsState()
    val listState = rememberLazyListState()
    var isAddScreenVisible by remember { mutableStateOf(false) }

    val openAddScreen: (UUID?) -> Unit = { itemId ->
        isAddScreenVisible = true
        navToAdd(itemId)
    }

    val closeAddScreen: () -> Unit = {
        isAddScreenVisible = false
    }

    Scaffold(
        containerColor = colorResource(id = R.color.backPrimaryColor),
        topBar = {
            AppBar(
                scrollBehavior = scrollBehavior,
                onVisibleClick = { viewModel.switchVisibility() },
                onSettingsClick = navToSetting,
                onAboutAppClick = navToAboutApp,
                isVisibleAll = todoItemScreenUiState.value.isVisibleDone,
                doneCount = todoItemScreenUiState.value.numberOfDoneTodoItem,
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(MaterialTheme.colorScheme.background),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { openAddScreen(null) },
                containerColor = colorResource(id = R.color.blue),
                contentColor = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .alpha(if (isAddScreenVisible) 0f else 1f),
                shape = CircleShape,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = stringResource(id = R.string.descriptionButtonNavToAdd),
                    tint = Colors.White
                )
            }

            AnimatedVisibility(
                visible = isAddScreenVisible,
                enter = fadeIn(animationSpec = tween(durationMillis = 500)),
                exit = fadeOut(animationSpec = tween(durationMillis = 500))
            ) {
                AddTodoItemScreen(
                    item = null,
                    viewModel = hiltViewModel(),
                    onBack = closeAddScreen
                )
            }
        }
    ) { innerPadding ->
        if (todoItemScreenUiState.value.loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressBar()
            }
        } else {
            Column(
                Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize()
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding)
                        .padding(start = 16.dp, end = 16.dp)
                        .zIndex(1f)
                ) {
                    itemsIndexed(
                        items = todoItemScreenUiState.value.currentTodoItemList,
                    ) { _, item ->
                        TodoItemCard(
                            item,
                            openAddScreen,
                            { viewModel.updateTodoItem(it) },
                            { viewModel.formatDate(it) },
                            { flag, relevance ->
                                viewModel.designOfCheckboxes(
                                    flag,
                                    relevance
                                )
                            })
                    }
                    item {
                        UpdateTaskItem { viewModel.updateTasks() }
                    }
                }
                if (todoItemScreenUiState.value.errorMessage != null) {
                    SnackbarForError(viewModel)
                    viewModel.retryFunction()
                }
            }
        }
    }
}


@Composable
fun UpdateTaskItem(
    onClick: () -> Unit
) {
    val contentDescriptionButtonUpdate = stringResource(id = R.string.descriptionButtonUpdate)
    Button(
        onClick = { onClick() },
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 32.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.semantics {
            contentDescription = contentDescriptionButtonUpdate
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .focusable(true)
                .animateContentSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.Start,

            ) {
            Text(
                text = stringResource(id = R.string.update),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}