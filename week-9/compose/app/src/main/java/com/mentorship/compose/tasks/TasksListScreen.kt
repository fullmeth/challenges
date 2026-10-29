@file:OptIn(ExperimentalMaterial3Api::class)

package com.mentorship.compose.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import com.mentorship.compose.domain.entity.Task
import com.mentorship.compose.domain.entity.TaskFilter
import com.mentorship.compose.domain.entity.formatDate
import com.mentorship.compose.ui.components.CustomCard
import com.mentorship.compose.ui.components.StatusChip
import com.mentorship.compose.ui.theme.ComposeTheme

@Composable
fun TasksListScreen(
    viewModel: TasksListViewModel = hiltViewModel(),
    onTaskClick: (Int) -> Unit,
    onFilterClick: () -> Unit,
    onBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val filters by viewModel.appliedFilters.collectAsStateWithLifecycle()
    TasksListScreen(state, filters, onTaskClick, onFilterClick, onBack)
}

@Composable
private fun TasksListScreen(
    state: List<Task>,
    filters: TaskFilter,
    onTaskClick: (Int) -> Unit,
    onFilterClick: () -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tasks",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
                            tint = MaterialTheme.colorScheme.onBackground,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onFilterClick) {
                        Box {
                            Icon(
                                modifier = Modifier
                                    .size(16.dp)
                                    .align(Alignment.Center),
                                imageVector = Icons.Rounded.FilterList,
                                tint = MaterialTheme.colorScheme.onBackground,
                                contentDescription = null
                            )
                            if (filters.isActive) {
                                Icon(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .align(Alignment.TopEnd),
                                    imageVector = Icons.Rounded.Circle,
                                    tint = MaterialTheme.colorScheme.primary,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                },
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            contentPadding = it,
            verticalArrangement = spacedBy(16.dp)
        ) {
            items(state) { task ->
                Task(
                    modifier = Modifier.fillMaxWidth(),
                    task = task,
                    onTaskClick = onTaskClick,
                )
            }
        }
    }
}

@Composable
fun Task(
    modifier: Modifier = Modifier,
    task: Task,
    onTaskClick: (Int) -> Unit,
) {
    CustomCard(
        modifier = modifier,
        onClick = dropUnlessResumed { onTaskClick(task.id) },
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
            StatusChip(status = task.status)
        }
        Text(
            text = task.description,
            style = MaterialTheme.typography.bodySmall,
        )
        Text(
            text = "${task.assignee.displayName} · ${task.dueDate.formatDate()} · ${task.priority}",
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

@Preview
@Composable
private fun Preview() {
    ComposeTheme {
        TasksListScreen(
            listOf(Task.mock(), Task.mock(), Task.mock(), Task.mock()),
            TaskFilter.none(),
            {},
            { },
            {})
    }
}