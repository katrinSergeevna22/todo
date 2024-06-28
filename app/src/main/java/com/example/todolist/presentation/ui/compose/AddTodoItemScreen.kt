import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.todolist.domain.TodoItem
import com.example.todolist.presentation.viewModel.ListViewModel
import com.example.todolist.R
import com.example.todolist.domain.Relevance
import com.example.todolist.ui.theme.Colors
import java.text.SimpleDateFormat
import java.util.*


//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoItemScreen(
    item: TodoItem?,
    size: Int,
    navController: NavHostController) {
    var text by remember { mutableStateOf(item?.text ?: "") }
    var selectedImportance by remember { mutableStateOf(
        if(item?.relevance == Relevance.LOW.getRelevance()) Relevance.LOW.ordinal
        else if(item?.relevance == Relevance.URGENT.getRelevance()) Relevance.URGENT.ordinal
        else 0) }

    var isDeadlineEnabled by remember { mutableStateOf(if(item==null) false else item?.deadline!=null)} //?: false) }
    var selectedDate by remember { mutableStateOf(item?.deadline ?: "") }

    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())

    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            selectedDate = dateFormat.format(calendar.time)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH),


    )

    val context = LocalContext.current
    val viewModel = ListViewModel.newInstance()
    val scrollState = rememberScrollState()
    val toolbarElevation by animateDpAsState(if (scrollState.value > 0) 4.dp else 0.dp)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },

                actions = {
                    TextButton(onClick = {
                        if (text.isNotEmpty()) {
                            if (item == TodoItem() || item == null) {
                                val newItem = TodoItem(
                                    (size).toString(),
                                    text,
                                    if (selectedImportance == -1 || selectedImportance == 0) "Нет"
                                    else if (selectedImportance == 1) "Низкий"
                                    else "!! Высокий",
                                    deadline = if (selectedDate != "") selectedDate else null,
                                    executionFlag = false,
                                    dateFormat.toString(),
                                    null,
                                )

                                viewModel.addItem(newItem)
                                Toast.makeText(context, "Todo Item Added", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                val newItem = TodoItem(
                                    item.id,
                                    text,
                                    if (selectedImportance == -1 || selectedImportance == 0) "Нет"
                                    else if (selectedImportance == 1) "Низкий"
                                    else "!! Высокий",
                                    deadline = if (selectedDate != "") selectedDate else null,
                                    executionFlag = item.executionFlag,
                                    item.dateOfCreating,
                                    dateFormat.toString(),
                                )
                                viewModel.editItem(newItem)
                                Toast.makeText(context, "Todo Item Updated", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            navController.popBackStack()
                        } else {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                    ) {
                        Text(
                            text = stringResource(id = R.string.save),
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp,
                            color = Colors.Blue,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }
                },

                colors = TopAppBarDefaults.largeTopAppBarColors(MaterialTheme.colorScheme.background),

                //elevation = toolbarElevation
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

            BasicTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    //.background(MaterialTheme.colorScheme.secondary)
                    //.background(colorResource(id = R.color.backSecondaryColor))
                    .padding(16.dp)
                    .heightIn(min = 104.dp),

                textStyle = LocalTextStyle.current.copy(
                    fontSize = 16.sp,
                    fontFamily = FontFamily.SansSerif,
                    color = MaterialTheme.colorScheme.primary
                ),


                decorationBox = { innerTextField ->
                    if (text.isEmpty()) {
                        Text(
                            text = stringResource(id = R.string.hintForEditMultiText),
                            style = MaterialTheme.typography.bodySmall,
                            color = Colors.GrayColor,
                            //style = LocalTextStyle.current.copy(color = Color.Gray)
                        )
                    }
                    innerTextField()
                }
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = stringResource(id = R.string.importance),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            SpinnerRelevance(selectedImportance, onItemSelected = { selectedImportance = it })

            //Spacer(modifier = Modifier.height(16.dp))

            Divider(
                color = MaterialTheme.colorScheme.onSurface,
                thickness = 1.dp,)
                //modifier=Modifier.padding(top=16.dp))

            Spacer(modifier = Modifier.height(26.dp))

            Row(
                modifier=Modifier.fillMaxWidth(),
                verticalAlignment=Alignment.CenterVertically
            ) {
                Text(
                    text=stringResource(id=R.string.make_it_to),
                    style = MaterialTheme.typography.titleMedium,
                    color=MaterialTheme.colorScheme.primary,
                    //modifier=Modifier.padding(start=16.dp)
                )

                Spacer(modifier=Modifier.weight(1f))

                Switch(
                    checked=isDeadlineEnabled,
                    onCheckedChange={
                        isDeadlineEnabled=it
                        if (it) {
                            datePickerDialog.show()
                        } else {
                            selectedDate=""
                        }
                    },
                    colors=SwitchDefaults.colors(
                        checkedThumbColor=Colors.Blue,
                        uncheckedThumbColor=Color.White,
                        uncheckedTrackColor=Color.LightGray,
                        checkedTrackColor= Colors.LightBlue,
                        uncheckedBorderColor=Color.LightGray
                    ),

                )
            }

            if (isDeadlineEnabled && selectedDate.isNotEmpty()) {
                Text(
                    text=selectedDate,
                    style = MaterialTheme.typography.bodySmall,
                    //fontFamily=FontFamily.SansSerif,
                    //fontSize=16.sp,
                    color = Colors.Blue,
                    modifier=Modifier.padding(top=4.dp)
                )
            }

            Divider(
                //color=colorResource(id=R.color.separatorColor),
                color = MaterialTheme.colorScheme.onSurface,
                thickness=1.dp,
                modifier=Modifier.padding(top=51.dp)
            )

            Row(
                modifier=Modifier.fillMaxWidth(),
                verticalAlignment=Alignment.CenterVertically,
            ) {
                IconButton(onClick={
                    if(item!= TodoItem()) {
                        run {
                            viewModel.removeItem(item ?: TodoItem())
                            navController.popBackStack()
                        }
                    }
                },
                modifier= Modifier
                    .padding(top = 22.dp)
                    .size(24.dp)
                ){
                    Icon(
                        painter=painterResource(id=R.drawable.ic_delete),
                        contentDescription="Кнопка удалить",
                        tint=
                        if(item!=TodoItem())
                            Colors.Red
                            //colorResource(id=R.color.red)
                        else
                            Colors.GrayColor,
                            //colorResource(id=R.color.grey),
                        //modifier= Modifier
                            //.padding(top = 22.dp)
                            //.size(24.dp)
                    )
                }

                TextButton(onClick = {
                    if(item!= TodoItem()) {
                        run {
                            viewModel.removeItem(item ?: TodoItem())
                            navController.popBackStack()
                        }
                    }
                }) {
                    Text(
                        text = stringResource(id = R.string.delete),
                        style = MaterialTheme.typography.titleMedium,
                        //fontFamily = FontFamily.SansSerif,
                        //fontWeight = FontWeight.Black,
                        //fontSize = 20.sp,
                        color = if(item!= TodoItem())
                            Colors.Red
                            //colorResource(id=R.color.red)
                        else
                            Colors.GrayColor,
                            //colorResource(id=R.color.grey),
                        modifier = Modifier.padding(start = 12.dp, top = 22.dp),

                        )
                }
            }


        }
    }
}

@Composable
fun SpinnerRelevance(selectedIndex: Int, onItemSelected: (Int) -> Unit) {
    val items=listOf("Нет", "Низкий", "!! Высокий")

    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier=Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterStart) {

        TextButton(
            onClick={expanded=true},
            //modifier=Modifier.fillMaxWidth(),
        ) {
            Text(
                text=items[selectedIndex],
                //color=colorResource(id=R.color.grey),
                color=Colors.GrayColor,
                //modifier=Modifier.align(Alignment.CenterStart), //align(Alignment.Start).padding(start=16.dp)
                        //align(Alignment.Start).padding(start=16.dp)
                //fontSize = 16.sp,
                //fontFamily = FontFamily.Default
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        DropdownMenu(
            expanded=expanded,
            onDismissRequest={expanded=false},

        ) {
            items.forEachIndexed { index, item->
                DropdownMenuItem({
                    Text(text=item,
                        color=if (index == items.size - 1) Colors.Red else MaterialTheme.colorScheme.onPrimary,
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Default)
                },
                    onClick={
                        //Log.d("SelectInd", selectedIndex.toString())
                    onItemSelected(index)
                    expanded=false
                })
            }
        }
    }
}