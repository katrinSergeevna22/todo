package com.example.todolist.presentation.ui.compose.add_item

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.todolist.R
import com.example.todolist.presentation.ui.theme.Colors
import com.example.todolist.presentation.viewModel.AddTodoItemViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun AddTodoScreenDeadline(viewModel: AddTodoItemViewModel) {
    val data = viewModel.getAddTodoUiState()
    var isDeadlineEnabled by remember { mutableStateOf(data.value.deadline != null && data.value.deadline != 0L) } //?: false) }
    var selectedDate by remember { mutableStateOf(data.value.deadline) }

    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            selectedDate = calendar.timeInMillis
            viewModel.setDeadline(selectedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH), )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.make_it_to),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            //modifier=Modifier.padding(start=16.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Switch(
            checked = isDeadlineEnabled,
            onCheckedChange = {
                isDeadlineEnabled = it
                if (it) {
                    datePickerDialog.show()

                } else {
                    selectedDate = null
                    viewModel.setDeadline(selectedDate)
                }
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Colors.Blue,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray,
                checkedTrackColor = Colors.LightBlue,
                uncheckedBorderColor = Color.LightGray
            ),

            )
    }

    if (isDeadlineEnabled && selectedDate != null) {
        Text(
            text = viewModel.formatDate(selectedDate),
            style = MaterialTheme.typography.bodySmall,
            color = Colors.Blue,
            modifier = Modifier.padding(top = 4.dp)
        )
    }


}