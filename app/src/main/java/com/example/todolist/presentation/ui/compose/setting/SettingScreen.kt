package com.example.todolist.presentation.ui.compose.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.todolist.R
import com.example.todolist.presentation.ui.theme.Colors
import com.example.todolist.presentation.ui.theme.ThemeOption
import com.example.todolist.presentation.viewModel.SettingViewModel

@Composable
fun SettingScreen(
    settingViewModel: SettingViewModel,
    onBack: () -> Unit
) {
    var selectedTheme by remember { mutableStateOf(settingViewModel.getTheme()) }

    Scaffold(
        topBar = {
            SettingToolbar {
                onBack()
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.selectedTheme),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.padding(8.dp))

            RadioGroup(
                options = ThemeOption.entries.toTypedArray(),
                selectedOption = selectedTheme,
                onOptionSelected = {
                    selectedTheme = it
                },
                {
                    settingViewModel.getTitleItems(it)
                }
            )
            val contentDescriptionButtonSelect =
                stringResource(
                    id = R.string.descriptionButtonApply,
                    settingViewModel.getTitleItems(selectedTheme)
                )
            Button(
                onClick = {
                    settingViewModel.setAppTheme(selectedTheme)
                },
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
                    .height(60.dp)
                    .semantics { contentDescription = contentDescriptionButtonSelect },
                colors = ButtonDefaults.buttonColors(Colors.Blue)
            ) {
                Text(
                    text = stringResource(id = R.string.buttonApply).uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    color = Colors.White,
                    modifier = Modifier.clearAndSetSemantics { },
                )
            }
        }
    }
}


@Composable
fun RadioGroup(
    options: Array<ThemeOption>,
    selectedOption: ThemeOption,
    onOptionSelected: (ThemeOption) -> Unit,
    themeToString: (ThemeOption) -> String,
) {
    options.forEach { option ->
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = option == selectedOption,
                colors = RadioButtonDefaults.colors(Colors.Blue),
                onClick = {
                    onOptionSelected(option)
                },
                modifier = Modifier.semantics {
                    contentDescription = themeToString(option)
                }
            )
            Text(
                text = themeToString(option),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}


