package com.example.todolist.presentation.ui.compose.setting

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import com.example.todolist.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingToolbar(
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    val toolbarElevation by animateDpAsState(if (scrollState.value > 0) 4.dp else 0.dp)
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { onBack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = stringResource(id = R.string.descriptionButtonCloseSettingScreen),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(24.dp)
                        .semantics { traversalIndex = 1f }
                )
            }
        },
        title = {
            Text(
                text = stringResource(id = R.string.setting),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.semantics { traversalIndex = 2f }
            )
        },
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        colors = TopAppBarDefaults.largeTopAppBarColors(MaterialTheme.colorScheme.background),
        modifier = Modifier
            .shadow(elevation = toolbarElevation)
            .semantics { isTraversalGroup = true }
    )
}
