package com.example.todolist.presentation.ui.compose.todo_list

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.todolist.R
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.example.todolist.presentation.ui.theme.Colors
import com.example.todolist.presentation.viewModel.ListViewModel
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import java.util.UUID

@SuppressLint("StaticFieldLeak")

//val todoList by viewModel.newTodoList.collectAsState(coroutineContext)
@OptIn(ExperimentalMaterial3Api::class)
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TodoListScreen(
    viewModel: ListViewModel,
    navToAdd: (UUID?) -> Unit,
) {

    val scrollBehavior =
        TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val todoItemScreenUiState = viewModel.getTodoItemsScreenUiState().collectAsState()
    val listState = rememberLazyListState()

    Scaffold(

        containerColor = colorResource(id = R.color.backPrimaryColor),
        topBar = {
            TodoListToolBar(scrollBehavior = scrollBehavior, viewModel)
        },

        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(MaterialTheme.colorScheme.background),
        floatingActionButton = {

            FloatingActionButton(
                onClick = { navToAdd(null) },
                containerColor = colorResource(id = R.color.blue),
                contentColor = Color.White,
                modifier = Modifier.padding(16.dp),
                shape = CircleShape,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Кнопка для добавления нового дела",
                    tint = Colors.White
                )
            }
        }
    ) { innerPadding ->

        if (!todoItemScreenUiState.value.loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressBar()
            }
        } else {
            Column {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding)
                        .padding(start = 16.dp, end = 16.dp)
                        .zIndex(1f)
                    //.background(MaterialTheme.colorScheme.surface)
                ) {

                    itemsIndexed(
                        items = todoItemScreenUiState.value.currentTodoItemList,


                    ) { _, item ->

                        TodoItemCard(
                            item,
                            navToAdd,
                            { it -> viewModel.editExecutionFlow(it) },
                            { it -> viewModel.formatDate(it) })
                    }

                    item {
                        NewTaskItem { viewModel.updateTasks() }
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
fun NewTaskItem(
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .focusable(true)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        //Spacer(modifier = Modifier.width(36.dp))
        Text(
            modifier = Modifier,
            text = stringResource(id = R.string.update),
            color = MaterialTheme.colorScheme.primary,
            //style = TasksTheme.typography.body,
        )
    }
}
/*
@Composable
fun HeaderWithAnimation() {

    val todoList by viewModel.todoList.collectAsState()
    val isVisibilityOn by viewModel.isVisibilityOnFlow.collectAsState()

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val isHeaderLarge by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }
    val completedCount by viewModel.completedCount.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        AnimatedVisibility(
            visible = isHeaderLarge,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            //HeaderLarge(completedCount, viewModel::switchVisibility)
        }

        AnimatedVisibility(
            visible = !isHeaderLarge,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300))
        ) {

            //HeaderSmall(completedCount, viewModel::switchVisibility)
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp)
            //.background(MaterialTheme.colorScheme.surface)
        ) {
            itemsIndexed(
                todoList
            ) { _, item ->
                TodoItemCard(item, navController = navigationControler)
            }
            items(2) {
                Spacer(modifier = Modifier.height(25.dp))
            }

        }

    }
}

/*
@Composable
fun HeaderLarge(
    size: Int,
    //isVisibilityOn: Boolean,
    onToggleVisibility: () -> Unit) {
    val isVisibilityOn by viewModel.isVisibilityOnFlow.collectAsState()
    var checked by remember { mutableStateOf(isVisibilityOn) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(164.dp)
            .padding(start = 60.dp),
        contentAlignment = Alignment.CenterStart,

    ) {
        Column {
            Text(
                text = stringResource(id=R.string.my_case),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id=R.string.done) + " - " + size,
                    style = MaterialTheme.typography.bodySmall,
                    color = Colors.GrayColor,
                )
                IconToggleButton(
                    checked = checked,
                    onCheckedChange = {

                        onToggleVisibility()
                        checked = it},

                    modifier = Modifier
                        .padding(end = 24.dp)
                        .size(24.dp)
                ) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(id = if (!checked) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                        //painter = painterResource(id = R.drawable.ic_info_outline),
                        //painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "Кнопка показа всех дел",
                        //contentScale = ContentScale.Crop,

                        tint = Colors.Blue,
                    )
                }


            }
        }
    }
}

 */
/*
@Composable
fun HeaderSmall(
    size: Int,
    //isVisibilityOn: Boolean,
    onToggleVisibility: () -> Unit) {

    val isVisibilityOn by viewModel.isVisibilityOnFlow.collectAsState()
    var checked by remember { mutableStateOf(isVisibilityOn) }
    Box(
        modifier = Modifier
            .padding((WindowInsets.systemBars.only(WindowInsetsSides.Top)).asPaddingValues())
            //modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(MaterialTheme.colorScheme.background),
           // .padding(start = 16.dp, end = 24.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id=R.string.my_case),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            IconToggleButton(
                checked = checked,
                onCheckedChange = {
                    onToggleVisibility()
                    checked = it},

                modifier = Modifier
                    .size(24.dp)
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = if (!checked) R.drawable.ic_visibility else R.drawable.ic_visibility_off),

                    contentDescription = "Кнопка показа скрытых дел",
                    //contentScale = ContentScale.Crop,

                    tint = Colors.Blue,

                    )
            }
        }
    }

}
 */