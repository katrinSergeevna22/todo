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
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.todolist.R
import com.example.todolist.domain.model.TodoModel
import com.example.todolist.presentation.ui.compose.add_item.AddScreenTextField
import com.example.todolist.presentation.ui.compose.add_item.AddScreenToolbar
import com.example.todolist.presentation.ui.compose.add_item.AddTodoItemRelevance
import com.example.todolist.presentation.ui.compose.add_item.AddTodoScreenDeadline
import com.example.todolist.presentation.ui.theme.Colors
import com.example.todolist.presentation.viewModel.AddTodoItemViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoItemScreen(
    item: TodoModel?,
    //size: Int,
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
                //.background(colorResource(id = R.color.backPrimaryColor))
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
        ) {

            val setTextToState: (String) -> Unit = { text -> viewModel.setText(text) }
            AddScreenTextField(item = item, setTextToState, viewModel.getAddTodoUiState())
            Spacer(modifier = Modifier.height(28.dp))

            AddTodoItemRelevance(viewModel)

            Divider(
                color = MaterialTheme.colorScheme.onSurface,
                thickness = 1.dp,
            )

            Spacer(modifier = Modifier.height(26.dp))

            AddTodoScreenDeadline(viewModel = viewModel)

            Divider(
                //color=colorResource(id=R.color.separatorColor),
                color = MaterialTheme.colorScheme.onSurface,
                thickness = 1.dp,
                modifier = Modifier.padding(top = 51.dp, bottom = 8.dp)
            )

            var isButtonPressed by remember { mutableStateOf(false) }

            Button(
                onClick = {
                    isButtonPressed = !isButtonPressed
                    if (item != null) {
                        run {
                            viewModel.removeFlow(item)
                            onBack()
                        }
                    }
                },
                //modifier = Modifier.background(MaterialTheme.colorScheme.background),
                elevation = ButtonDefaults.elevatedButtonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 32.dp
                ),
                colors =
                if (!isButtonPressed) {
                    ButtonDefaults.buttonColors(MaterialTheme.colorScheme.background)
                }
                else {
                    ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    //.background(MaterialTheme.colorScheme.background),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = stringResource(id = R.string.descriptionButtonDelete),
                        modifier = Modifier
                            //.padding(top = 22.dp)
                            .size(24.dp),
                        tint =
                        if (item != null){
                            if (!isButtonPressed) {
                                Colors.Red
                            }
                            else {
                                Colors.White
                            }
                        }
                        else
                            Colors.GrayColor,
                    )
                    Text(
                        text = stringResource(id = R.string.delete),
                        style = MaterialTheme.typography.titleMedium,
                        color =
                        if (item != null){
                            if (!isButtonPressed) {
                                Colors.Red
                            }
                            else {
                                Colors.White
                            }
                        }
                        //colorResource(id=R.color.red)
                        else
                            Colors.GrayColor,
                        modifier = Modifier.padding(start = 12.dp),
                    )
                }
            }
        }
    }
}

