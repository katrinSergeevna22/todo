package com.example.todolist.presentation.ui.compose.todo_list

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.todolist.R
import com.example.todolist.domain.Relevance
import com.example.todolist.domain.model.TodoItem
import com.example.todolist.domain.textName
import com.example.todolist.domain.textNameForJson
import com.example.todolist.presentation.ui.theme.Colors
import java.util.UUID


@Composable
    fun TodoItemCard(
    item : TodoItem,
    navToAdd: (UUID?) -> Unit,
    onClickDone: (TodoItem) -> Unit,
    formatDate: (Long?) -> String) {

        var checked by remember { mutableStateOf(item.executionFlag) }
        var selectRelevance by remember { mutableStateOf(item.relevance) }
        val context = LocalContext.current
        Card(
            modifier = Modifier
                .fillMaxWidth(),

            colors = CardDefaults.cardColors(
                MaterialTheme.colorScheme.surface,
                colorResource(id = R.color.black),
                MaterialTheme.colorScheme.surface,
                colorResource(id = R.color.black),
            )
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                val (readinessFlag, textOfTodoItem, infoButton, dateTodoItem, iconInText) = createRefs()

                IconToggleButton(
                    checked = checked,
                    onCheckedChange = {
                        checked = it
                        item.executionFlag = !item.executionFlag
                        onClickDone(item) },
                    modifier = Modifier
                        .size(24.dp)
                        .constrainAs(readinessFlag) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        }
                    ) {
                    Image(
                        painter =
                        if (item.executionFlag){
                            painterResource(id = R.drawable.ic_readliness_flag_checked)
                        }
                        else if (item.relevance == Relevance.URGENT.textNameForJson(context)){
                            painterResource(id = R.drawable.ic_readliness_flag_unchecked_high)
                        }
                        else {
                            Log.d("CardRel", item.relevance)
                            painterResource(id = R.drawable.ic_readiness_flag_normal)
                        },
                        contentDescription = stringResource(id = R.string.descriptionButtonReady),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(24.dp),
                        colorFilter =
                        if (!item.executionFlag && item.relevance != stringResource(R. string.important)) {
                            ColorFilter.tint(Colors.GrayColor)
                        } else {
                            null
                        }
                    )
                }

                if (item.executionFlag){
                    Text(
                        text = item.text,
                        fontFamily = FontFamily.Default,
                        color = Colors.GrayColor,
                        fontSize = 16.sp,
                        style = TextStyle(textDecoration = TextDecoration.LineThrough),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .constrainAs(textOfTodoItem) {
                                top.linkTo(parent.top)
                                start.linkTo(readinessFlag.end, margin = 12.dp)
                                end.linkTo(infoButton.start, margin = 12.dp)
                                width = Dimension.fillToConstraints
                            }
                    )
                }
                else {
                    Text(
                        text = item.text,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .constrainAs(textOfTodoItem) {
                                top.linkTo(parent.top)
                                start.linkTo(readinessFlag.end, margin = 12.dp)
                                end.linkTo(infoButton.start, margin = 12.dp)
                                width = Dimension.fillToConstraints
                            }

                    )
                }

                IconButton(
                    onClick = { navToAdd(item.id) },
                    modifier = Modifier
                        .size(24.dp)
                        //.background(Color.Transparent)
                        .constrainAs(infoButton) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_info_outline),
                        contentDescription = "Кнопка для редактирования",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Log.d("Deadline", item.deadline.toString())
                if (item.deadline != 0L && item.deadline != null) {
                    Text(
                        text =  formatDate(item.deadline),
                        fontFamily = FontFamily.Default,
                        color = Colors.GrayColor,
                        modifier = Modifier.constrainAs(dateTodoItem) {
                            top.linkTo(textOfTodoItem.bottom, margin = 4.dp)
                            start.linkTo(readinessFlag.end, margin = 12.dp)
                        }

                    )
                }
            }
        }
    }
