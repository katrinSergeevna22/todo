package com.example.todolist.presentation.ui.compose.todo_list

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.todolist.R
import com.example.todolist.presentation.ui.theme.Colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onVisibleClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onAboutAppClick: () -> Unit,
    isVisibleAll: Boolean,
    doneCount: Int,
    modifier: Modifier = Modifier
) {
    val elevation = animateDpAsState(
        targetValue = if (scrollBehavior.state.collapsedFraction > 0.5) {
            20.dp
        } else {
            0.dp
        },
        animationSpec = tween(durationMillis = 400)
    )
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 0.dp)
            .shadow(elevation.value)
    ) {
        LargeTopAppBar(
            actions = {},
            colors = TopAppBarDefaults.largeTopAppBarColors(
                scrolledContainerColor = MaterialTheme.colorScheme.background,
                containerColor = MaterialTheme.colorScheme.background
            ),
            title = {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    ) {
                        if (scrollBehavior.state.collapsedFraction >= 0.5) {
                            Text(
                                text = stringResource(R.string.my_case),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        } else {
                            Column {
                                Text(
                                    text = stringResource(id = R.string.my_case),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                                Text(
                                    text = stringResource(id = R.string.done, doneCount),
                                    color = MaterialTheme.colorScheme.primary.copy(0.3f),
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }
                    }

                    IconButton(onClick = { onSettingsClick() },
                        modifier = Modifier.padding(start = 16.dp),
                        content = {
                            Icon(
                                tint = Colors.Blue,
                                painter = painterResource(R.drawable.ic_setting),
                                contentDescription = stringResource(id = R.string.descriptionButtonNavToSetting),
                            )
                        }
                    )

                    IconButton(onClick = { onAboutAppClick() }, content = {
                        Icon(
                            tint = Colors.Blue,
                            painter = painterResource(R.drawable.ic_app_info),
                            contentDescription = stringResource(id = R.string.descriptionButtonNavToAboutApp),
                        )
                    })

                    IconButton(onClick = { onVisibleClick() },
                        modifier = Modifier.padding(end = 16.dp),
                        content = {
                            Icon(
                                tint = Colors.Blue,
                                painter = painterResource(
                                    id = if (isVisibleAll) {
                                        R.drawable.ic_visibility_off
                                    } else {
                                        R.drawable.ic_visibility
                                    }
                                ),
                                contentDescription = stringResource(id = R.string.descriptionButtonSwitchVisibility),
                            )
                        }
                    )
                }
            },
            scrollBehavior = scrollBehavior
        )
    }
}
