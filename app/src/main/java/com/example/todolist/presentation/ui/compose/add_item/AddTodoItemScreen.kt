import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.todolist.domain.model.TodoItem
import com.example.todolist.R
import com.example.todolist.presentation.ui.compose.add_item.AddTodoItemRelevance
import com.example.todolist.presentation.ui.compose.add_item.AddTodoScreenDeadline
import com.example.todolist.presentation.ui.compose.add_item.AddScreenTextField
import com.example.todolist.presentation.ui.compose.add_item.AddScreenToolbar
import com.example.todolist.presentation.ui.theme.Colors
import com.example.todolist.presentation.viewModel.AddTodoItemViewModel

//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoItemScreen(
    item: TodoItem?,
    //size: Int,
    viewModel: AddTodoItemViewModel,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    viewModel.loadInState(item)
    var text by remember { mutableStateOf(item?.text ?: "") }
    var selectedImportance by remember { mutableStateOf(0) }
    //if(item?.relevance == Relevance.LOW.textName ) Relevance.LOW.ordinal
    //else if(item?.relevance == Relevance.URGENT.getRelevance()) Relevance.URGENT.ordinal
    //else 0) }

//    var isDeadlineEnabled by remember { mutableStateOf(if (item == null) false else item?.deadline != null) } //?: false) }
    var selectedDate by remember { mutableStateOf(item?.deadline ?: "") }
//
//    val calendar = Calendar.getInstance()
//    val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
//
//    val datePickerDialog = DatePickerDialog(
//        LocalContext.current,
//        { _, year, month, dayOfMonth ->
//            calendar.set(year, month, dayOfMonth)
//            selectedDate = dateFormat.format(calendar.time)
//        },
//        calendar.get(Calendar.YEAR),
//        calendar.get(Calendar.MONTH),
//        calendar.get(Calendar.DAY_OF_MONTH),
//
//
//        )


    val scrollState = rememberScrollState()
    val toolbarElevation by animateDpAsState(if (scrollState.value > 0) 4.dp else 0.dp)

    Scaffold(
        topBar = {
            AddScreenToolbar(
                item,
                onBack,
                viewModel,
            )
//
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

            val setTextToState : (String) -> Unit = { text -> viewModel.setText(text)}
            AddScreenTextField(item = item, setTextToState, viewModel)
            Spacer(modifier = Modifier.height(28.dp))

            AddTodoItemRelevance(viewModel)
            //Spacer(modifier = Modifier.height(16.dp))

            Divider(
                color = MaterialTheme.colorScheme.onSurface,
                thickness = 1.dp,
            )
            //modifier=Modifier.padding(top=16.dp))

            Spacer(modifier = Modifier.height(26.dp))

            AddTodoScreenDeadline(viewModel = viewModel)

            Divider(
                //color=colorResource(id=R.color.separatorColor),
                color = MaterialTheme.colorScheme.onSurface,
                thickness = 1.dp,
                modifier = Modifier.padding(top = 51.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = {
                        if (item != null) {
                            run {
                                viewModel.removeFlow(item)
                                onBack()
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(top = 22.dp)
                        .size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = stringResource(id = R.string.descriptionButtonDelete),
                        tint =
                        if (item != null)
                            Colors.Red
                        //colorResource(id=R.color.red)
                        else
                            Colors.GrayColor,
                    )
                }

                TextButton(onClick = {
                    if (item != null) {
                        run {
                            viewModel.removeFlow(item)
                            onBack()
                        }
                    }
                }) {
                    Text(
                        text = stringResource(id = R.string.delete),
                        style = MaterialTheme.typography.titleMedium,
                        color = if (item != null)
                            Colors.Red
                        else
                            Colors.GrayColor,
                        modifier = Modifier.padding(start = 12.dp, top = 22.dp),

                        )
                }
            }


        }
    }
}

