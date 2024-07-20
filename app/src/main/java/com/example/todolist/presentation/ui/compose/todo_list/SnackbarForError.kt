package com.example.todolist.presentation.ui.compose.todo_list

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.todolist.R
import com.example.todolist.presentation.ui.theme.Colors
import com.example.todolist.presentation.viewModel.ListViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SnackbarForError(viewModel: ListViewModel) {
    viewModel.getTodoItemsScreenUiState().value.errorMessage?.let { message ->

        Snackbar(
            modifier = Modifier
                .padding(8.dp)
                .border(1.dp, Color.Red),
            action = {
                Button(
                    onClick = {
                        viewModel.onClickRetry()
                        viewModel.clearErrorInDataFlow()
                    },
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(Colors.Red)
                ) {
                    Text(stringResource(id = R.string.repeat), color = Color.White)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(message)
                Spacer(Modifier.weight(1f))
                IconButton(
                    onClick = {
                        viewModel.clearErrorInDataFlow()
                    }, colors = IconButtonDefaults.iconButtonColors(Colors.Red)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }

}