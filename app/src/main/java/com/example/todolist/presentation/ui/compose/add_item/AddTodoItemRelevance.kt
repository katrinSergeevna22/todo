package com.example.todolist.presentation.ui.compose.add_item

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.R
import com.example.todolist.presentation.ui.theme.Colors
import com.example.todolist.presentation.viewModel.AddTodoItemViewModel

@Composable
fun AddTodoItemRelevance(
    viewModel: AddTodoItemViewModel
) {
    val data = viewModel.getAddTodoUiState()
    var selectedImportance by remember { mutableIntStateOf(data.value.relevance) }
    Text(
        text = stringResource(id = R.string.importance),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary
    )

    Spacer(modifier = Modifier.height(8.dp))
    SpinnerRelevance(selectedImportance, onItemSelected = { it ->
        selectedImportance = it
        viewModel.setImportant(selectedImportance)
    })
}

@Composable
fun SpinnerRelevance(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
) {
    val items = stringArrayResource(id = R.array.relevanceOfTodo)
    var expanded by remember { mutableStateOf(false) }
    val contentDescriptionSelectImportance =
        stringResource(id = R.string.select_importance)
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        TextButton(
            onClick = { expanded = true },
            modifier = Modifier.semantics {
                contentDescription = contentDescriptionSelectImportance
            }
        ) {
            Text(
                text = items[selectedIndex],
                color = Colors.GrayColor,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    {
                        Text(
                            text = item,
                            color = if (index == items.size - 1) Colors.Red else MaterialTheme.colorScheme.onPrimary,
                            fontSize = 16.sp,
                            fontFamily = FontFamily.Default
                        )
                    },
                    onClick = {
                        onItemSelected(index)
                        expanded = false
                    },
                )
            }
        }
    }
}