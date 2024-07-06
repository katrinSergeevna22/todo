package com.example.todolist.presentation.ui.compose.add_item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.R
import com.example.todolist.domain.model.TodoItem
import com.example.todolist.presentation.ui.theme.Colors
import com.example.todolist.presentation.viewModel.AddTodoItemViewModel

@Composable
fun AddScreenTextField(
    item: TodoItem?,
    setTextToState: (String) -> Unit,
    viewModel: AddTodoItemViewModel,
) {
    var text = viewModel.getAddTodoUiState().collectAsState()
    BasicTextField(
        value = text.value.description,
        onValueChange = {
            setTextToState(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
            .heightIn(min = 104.dp),

        textStyle = LocalTextStyle.current.copy(
            fontSize = 16.sp,
            fontFamily = FontFamily.SansSerif,
            color = MaterialTheme.colorScheme.primary
        ),


        decorationBox = { innerTextField ->
            if (text.value.description == "") {
                Text(
                    text = stringResource(id = R.string.hintForEditMultiText),
                    style = MaterialTheme.typography.bodySmall,
                    color = Colors.GrayColor,
                )
            }
            innerTextField()
        }
    )
}
