import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.todolist.R
import com.example.todolist.domain.model.TodoModel
import com.example.todolist.presentation.ui.compose.add_item.AddScreenTextField
import com.example.todolist.presentation.ui.compose.add_item.AddScreenToolbar
import com.example.todolist.presentation.ui.compose.add_item.AddTodoItemRelevance
import com.example.todolist.presentation.ui.compose.add_item.AddTodoScreenDeadline
import com.example.todolist.presentation.ui.theme.Colors
import com.example.todolist.presentation.viewModel.AddTodoItemViewModel

@Composable
fun AddTodoItemScreen(
    item: TodoModel?,
    viewModel: AddTodoItemViewModel,
    onBack: () -> Unit,
) {
    viewModel.loadInState(item)
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            AddScreenToolbar(
                item,
                onBack,
                viewModel,
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
        ) {
            val setTextToState: (String) -> Unit = { text -> viewModel.setText(text) }
            AddScreenTextField(setTextToState, viewModel.getAddTodoUiState())
            Spacer(modifier = Modifier.height(28.dp))

            AddTodoItemRelevance(viewModel)

            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(26.dp))

            AddTodoScreenDeadline(viewModel = viewModel)

            HorizontalDivider(
                modifier = Modifier.padding(top = 51.dp, bottom = 8.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface
            )

            var isButtonPressed by remember { mutableStateOf(false) }
            val descriptionButtonDelete =
                stringResource(id = R.string.descriptionButtonDelete)
            val toastDelete = stringResource(id = R.string.toastDelete)
            val context = LocalContext.current

            Button(
                onClick = {
                    isButtonPressed = !isButtonPressed
                    if (item != null) {
                        run {
                            viewModel.removeFlow(item)
                            onBack()
                        }
                        Toast.makeText(context, toastDelete, Toast.LENGTH_SHORT).show()
                    }

                },

                elevation = ButtonDefaults.elevatedButtonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 32.dp
                ),
                enabled = item != null,
                colors =
                if (!isButtonPressed) {
                    ButtonDefaults.buttonColors(MaterialTheme.colorScheme.background)
                } else {
                    ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                },
                modifier = Modifier.semantics {
                    contentDescription = descriptionButtonDelete
                },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp),
                        tint =
                        if (item != null) {
                            if (!isButtonPressed) {
                                Colors.Red
                            } else {
                                Colors.White
                            }
                        } else
                            Colors.GrayColor,
                    )
                    Text(
                        text = stringResource(id = R.string.delete),
                        style = MaterialTheme.typography.titleMedium,
                        color =
                        if (item != null) {
                            if (!isButtonPressed) {
                                Colors.Red
                            } else {
                                Colors.White
                            }
                        } else
                            Colors.GrayColor,
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .clearAndSetSemantics { },
                    )
                }
            }
        }
    }
}

