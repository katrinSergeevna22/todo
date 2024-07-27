package com.example.todolist.presentation.ui.compose.add_item

import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.R
import com.example.todolist.domain.model.TodoModel
import com.example.todolist.presentation.ui.theme.Colors
import com.example.todolist.presentation.viewModel.AddTodoItemViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreenToolbar(
    item: TodoModel?,
    onBack: () -> Unit,
    viewModel: AddTodoItemViewModel,
) {
    var isPressed by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val toolbarElevation by animateDpAsState(if (scrollState.value > 0) 4.dp else 0.dp)
    val warningText = stringResource(id = R.string.textForEmptyToast)
    val warningTextAfterAdded = stringResource(id = R.string.toastAddOrUpdateNew)
    val descriptionButtonSave = stringResource(id = R.string.descriptionButtonSave)
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = { onBack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = stringResource(id = R.string.descriptionButtonCloseAddScreen),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(24.dp)
                        .semantics { traversalIndex = 1f }
                )
            }
        },
        actions = {
            Button(
                onClick = {
                    if (viewModel.textIsNotEmpty()) {
                        viewModel.saveItemByButton(
                            item
                        )
                        onBack()
                        Toast.makeText(
                            context,
                            warningTextAfterAdded,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        Toast.makeText(
                            context,
                            warningText,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                },
                elevation = ButtonDefaults.elevatedButtonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 32.dp
                ),
                colors =
                if (!isPressed) {
                    ButtonDefaults.buttonColors(MaterialTheme.colorScheme.background)
                } else {
                    ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surfaceTint)
                },
                modifier = Modifier
                    .testTag("addButton")
                    .semantics {
                        contentDescription = descriptionButtonSave
                        traversalIndex = 2f
                    },
            ) {
                Text(
                    text = stringResource(id = R.string.save).uppercase(),
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    color =
                    if (!isPressed) {
                        Colors.Blue
                    } else {
                        Colors.White
                    },
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clearAndSetSemantics { },
                )
            }
        },
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        colors = TopAppBarDefaults.largeTopAppBarColors(MaterialTheme.colorScheme.background),
        modifier = Modifier
            .shadow(elevation = toolbarElevation)
    )
}