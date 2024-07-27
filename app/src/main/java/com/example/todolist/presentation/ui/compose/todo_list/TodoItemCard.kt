package com.example.todolist.presentation.ui.compose.todo_list

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
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
import com.example.todolist.domain.model.TodoModel
import com.example.todolist.domain.textName
import com.example.todolist.presentation.ui.theme.Colors
import java.util.UUID


@Composable
fun TodoItemCard(
    item: TodoModel,
    navToAdd: (UUID?) -> Unit,
    onClickDone: (TodoModel) -> Unit,
    formatDate: (Long?) -> String,
    designOfCheckboxes: (Boolean, Relevance) -> Int,
) {
    var checked by remember { mutableStateOf(item.executionFlag) }
    val stateDescriptionOn = stringResource(id = R.string.stateDescriptionBtnReadyOn)
    val stateDescriptionOff = stringResource(id = R.string.stateDescriptionBtnReadyOff)
    val stateDescriptionImportance =
        stringResource(id = R.string.stateDescriptionBtnReadyImportance)
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
                    onClickDone(item)
                },

                modifier = Modifier
                    .size(24.dp)
                    .constrainAs(readinessFlag) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .semantics {
                        stateDescription =
                            if (item.executionFlag) stateDescriptionOn
                            else if (item.relevance == Relevance.URGENT) stateDescriptionImportance
                            else stateDescriptionOff
                    }
            ) {
                Image(
                    painter = painterResource(
                        id = designOfCheckboxes(
                            item.executionFlag,
                            item.relevance
                        )
                    ),
                    contentDescription = stringResource(
                        id = R.string.descriptionButtonReady,
                        item.text
                    ),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(24.dp),
                )
            }
            if (item.executionFlag) {
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
            } else {
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
            val isButtonPressed by remember { mutableStateOf(false) }
            val shadowElevation by animateDpAsState(targetValue = if (isButtonPressed) 8.dp else 0.dp)

            IconButton(
                onClick = { navToAdd(item.id) },
                colors =
                if (!isButtonPressed) {
                    IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.background)
                } else {
                    IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.surfaceTint)
                },
                modifier = Modifier
                    .size(24.dp)
                    .constrainAs(infoButton) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
                    .shadow(elevation = shadowElevation, shape = CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_info_outline),
                    contentDescription = stringResource(id = R.string.descriptionButtonEdit),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(24.dp),
                    colorFilter = ColorFilter.tint(Colors.GrayColor)
                )
            }
            if (item.deadline != 0L && item.deadline != null) {
                Text(
                    text = formatDate(item.deadline),
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
