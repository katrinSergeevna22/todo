package com.example.todolist.presentation.ui.compose.todo_list

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todolist.R
import com.example.todolist.presentation.ui.theme.Colors
import com.example.todolist.presentation.ui.theme.ToDoListComposeTheme
import com.example.todolist.presentation.viewModel.ListViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListToolBar(
    scrollBehavior: TopAppBarScrollBehavior,
    navToSetting: () -> Unit,
    navToDivKit: () -> Unit,
    viewModel: ListViewModel,
) {
    val isCollapsed = scrollBehavior.state.collapsedFraction > 0.5

    val textStyle =
        if (isCollapsed)
            MaterialTheme.typography.titleMedium
        else
            MaterialTheme.typography.titleLarge

    val elevation by animateDpAsState(
        targetValue = if (isCollapsed) 4.dp else 4.dp,
        label = ""
    )

    val state = viewModel.getTodoItemsScreenUiState().collectAsState()
    val completedCount by remember { mutableStateOf(viewModel.getTodoItemsScreenUiState().value.numberOfDoneTodoItem) }

    TopAppBar(
        modifier = Modifier.shadow(elevation),
        title = {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.Bottom,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                    //.verticalScroll(rememberScrollState()),

                ) {
                    Text(
                        text = stringResource(R.string.my_case),
                        style = textStyle,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                    /*
                    if (!isCollapsed) {
                        Text(
                            text = stringResource(
                                R.string.done
                            ) + " - " + completedCount,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                     */
                }

                IconButton(onClick = navToSetting, content = {
                    Icon(
                        tint = Colors.Blue,
                        painter = painterResource(R.drawable.ic_setting),
                        contentDescription = null,
                    )
                })
                IconButton(onClick = navToDivKit, content = {
                    Icon(
                        tint = Colors.Blue,
                        painter = painterResource(R.drawable.ic_app_info),
                        contentDescription = null,
                    )
                })
                IconButton(onClick = { viewModel.switchVisibility() },
                    content = {
                        Icon(
                            tint = Colors.Blue,
                            painter = painterResource(
                                id = if (state.value.isVisibleDone) R.drawable.ic_visibility
                                else
                                    R.drawable.ic_visibility_off
                            ),
                            contentDescription = null,
                        )
                    })
            }

        },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background,
            navigationIconContentColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.primary,
        ),
        scrollBehavior = scrollBehavior
    )
}
/*
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun LightToolBarPreview() {
    ToDoListComposeTheme(darkTheme = true) {
        TodoListToolBar(
            scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
                rememberTopAppBarState()
            ),
        )
    }
}

 */

