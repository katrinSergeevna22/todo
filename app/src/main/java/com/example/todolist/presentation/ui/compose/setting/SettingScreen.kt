package com.example.todolist.presentation.ui.compose.setting

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.R
import com.example.todolist.presentation.ui.compose.add_item.AddScreenToolbar
import com.example.todolist.presentation.ui.theme.Colors
import com.example.todolist.presentation.ui.theme.ThemeOption
import com.example.todolist.presentation.ui.theme.ToDoListComposeTheme
import com.example.todolist.presentation.viewModel.SettingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    settingViewModel: SettingViewModel,
    onBack: () -> Unit
) {
    var selectedTheme by remember { mutableStateOf(settingViewModel.getTheme()) }

    Scaffold(
        topBar = {
            val scrollState = rememberScrollState()
            val toolbarElevation by animateDpAsState(if (scrollState.value > 0) 4.dp else 0.dp)
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.setting),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                colors = TopAppBarDefaults.largeTopAppBarColors(MaterialTheme.colorScheme.background),
                modifier = Modifier
                    .shadow(elevation = toolbarElevation)
            )
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
                    Log.d("Button", selectedTheme.toString())
                    settingViewModel.getTitleItems(it)
                }
            )
            Button(
                onClick = {
                    settingViewModel.setAppTheme(selectedTheme) },
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(Colors.Blue)
            ) {
                Text(
                    text = stringResource(id = R.string.selectTheme),
                    style = MaterialTheme.typography.titleMedium,
                    color = Colors.White
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
                }
            )
            Text(
                text =  themeToString(option),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}


