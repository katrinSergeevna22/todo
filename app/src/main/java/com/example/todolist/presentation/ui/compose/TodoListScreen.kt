package com.example.todolistcompose.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.R
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavHostController
import com.example.todolist.domain.TodoItem
import com.example.todolist.presentation.viewModel.ListViewModel
import com.example.todolist.ui.theme.Colors

lateinit var navigationControler: NavHostController

//val todoList by viewModel.newTodoList.collectAsState(coroutineContext)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TodoListScreen(
    navController: NavHostController) {
    navigationControler = navController

    Scaffold(
        containerColor = colorResource(id = R.color.backPrimaryColor),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {navController.navigate("add_todo_item")},
                //onClick = { navController.navigate("add_todo_item") },
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
    ){
        //todoItemMutableList = todoItems
        HeaderWithAnimation() //,todoItemMutableList.size)


    }
}
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
            HeaderLarge(completedCount, viewModel::switchVisibility)
        }

        AnimatedVisibility(
            visible = !isHeaderLarge,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300))
        ) {

            HeaderSmall(completedCount, viewModel::switchVisibility)
        }

        LazyColumn(
            state =  listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp)
                //.background(MaterialTheme.colorScheme.surface)
        ) {
            itemsIndexed(
                todoList) { _, item ->
                TodoItemCard(item, navController = navigationControler )
            }
            items(2) {
                Spacer(modifier = Modifier.height(25.dp))
            }

        }
        errorMessage?.let { message ->
            Snackbar(
                modifier = Modifier.padding(8.dp),
                action = {
                    Button(onClick = { /* Handle Retry */ }) {
                        Text("Error")
                    }
                }
            ) { Text(message) }
        }

    }
}

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

@Composable
fun HeaderSmall(
    size: Int,
    //isVisibilityOn: Boolean,
    onToggleVisibility: () -> Unit) {

    val isVisibilityOn by viewModel.isVisibilityOnFlow.collectAsState()
    var checked by remember { mutableStateOf(isVisibilityOn) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 16.dp, end = 24.dp),
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