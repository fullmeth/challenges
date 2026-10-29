@file:OptIn(ExperimentalMaterial3Api::class)

package com.mentorship.compose.tasks.filters

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mentorship.compose.data.entity.User
import com.mentorship.compose.domain.entity.TaskFilter
import com.mentorship.compose.tasks.TasksListViewModel
import com.mentorship.compose.ui.components.CustomChip
import com.mentorship.compose.ui.components.Priority
import com.mentorship.compose.ui.components.Status
import com.mentorship.compose.ui.theme.Accent
import com.mentorship.compose.ui.theme.Accent100
import com.mentorship.compose.ui.theme.Accent2_100
import com.mentorship.compose.ui.theme.ComposeTheme
import com.mentorship.compose.ui.theme.Divider
import com.mentorship.compose.ui.theme.PillShape


@Composable
fun FiltersScreen(
    viewModel: TasksListViewModel,
    onBack: () -> Unit,
) {
    val users by viewModel.users.collectAsStateWithLifecycle()
    val draftFilters by viewModel.draftFilters.collectAsStateWithLifecycle()
    val filteredTasksCount by viewModel.filteredTasksCount.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.setDraftFilters()
    }

    FiltersScreen(
        users = users,
        filters = draftFilters,
        filteredTasksCount = filteredTasksCount,
        onStatusSelected = viewModel::onStatusSelected,
        onPrioritySelected = viewModel::onPrioritySelected,
        onAssigneeSelected = viewModel::onAssigneeSelected,
        onDueDateSelected = viewModel::onDueDateSelected,
        onApplyClick = {
            viewModel.onApplyClick()
            onBack()
        },
        onClearClick = viewModel::onClearClick,
        onBack = onBack
    )
}

@Composable
fun FiltersScreen(
    users: List<User>,
    filters: TaskFilter,
    filteredTasksCount: Int,
    onStatusSelected: (Status) -> Unit,
    onPrioritySelected: (Priority) -> Unit,
    onAssigneeSelected: (String) -> Unit,
    onDueDateSelected: (DueDate) -> Unit,
    onApplyClick: () -> Unit,
    onClearClick: () -> Unit,
    onBack: () -> Unit,
) {

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Filters",
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
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = spacedBy(8.dp)
            ) {
                item {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (filters.isActive) {
                            CustomChip(
                                containerColor = Color.Unspecified,
                                contentColor = Accent,
                                text = "Unsaved changes",
                                border = BorderStroke(1.dp, Accent)
                            )
                        } else {
                            Text("No filters applied")
                        }
                        TextButton(
                            onClick = onClearClick
                        ) {
                            Text(
                                text = "Clear all",
                                style = MaterialTheme.typography.titleSmall,
                                color = Accent,
                            )
                        }
                    }
                }
                item {
                    Text(text = "STATUS", color = Accent)
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = spacedBy(8.dp)
                    ) {
                        Status.entries.forEach { status ->
                            FilterChip(
                                shape = PillShape,
                                selected = status in filters.statuses,
                                onClick = { onStatusSelected(status) },
                                label = {
                                    Text(text = status.text)
                                }
                            )
                        }
                    }
                }
                item {
                    Text(text = "PRIORITY", color = Accent)
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = spacedBy(8.dp)
                    ) {
                        Priority.entries.forEach { priority ->
                            FilterChip(
                                shape = PillShape,
                                selected = priority in filters.priorities,
                                onClick = { onPrioritySelected(priority) },
                                label = {
                                    Text(text = priority.text)
                                }
                            )
                        }
                    }
                }
                item {
                    Text(text = "ASSIGNEE", color = Accent)
                }
                items(users) { item ->
                    AssigneeItem(
                        modifier = Modifier.fillMaxWidth(),
                        firstName = item.firstName,
                        lastName = item.lastName,
                        position = item.position,
                        selected = filters.assigneeId == item.id,
                        onClick = { onAssigneeSelected(item.id) }
                    )
                }
                item {
                    Text(text = "DUE DATE", color = Accent)
                }
                item {
                    SingleChoiceSegmentedButtonRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        DueDate.entries.forEachIndexed { index, item ->
                            SegmentedButton(
                                selected = item == filters.dueFilter,
                                onClick = { onDueDateSelected(item) },
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = DueDate.entries.size
                                ),
                                icon = {}
                            ) {
                                Text(item.label)
                            }
                        }
                    }
                }
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                onClick = onApplyClick
            ) {
                Text(
                    text = "Apply filters · $filteredTasksCount tasks",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                )
            }
        }

    }
}


enum class DueDate(val label: String) {
    AnyTime("Any time"), ThisWeek("This week"), Overdue("Overdue")
}

@Composable
fun AssigneeItem(
    modifier: Modifier = Modifier,
    firstName: String,
    lastName: String,
    position: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        border = BorderStroke(1.dp, if (selected) MaterialTheme.colorScheme.primary else Divider),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = if (selected) Accent100 else Color.Transparent),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(PillShape)
                    .background(Accent2_100)
                    .border(2.dp, MaterialTheme.colorScheme.surface, PillShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${firstName.first()}${lastName.first()}",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = spacedBy(4.dp)) {
                Text(
                    text = "$firstName $lastName",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = position,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            if (selected) {
                Icon(imageVector = Icons.Rounded.Check, contentDescription = null)
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ComposeTheme {
        FiltersScreen(
            users = listOf(User("John", "Travolta", "Powerful man")),
            filters = TaskFilter.none(),
            filteredTasksCount = 0,
            onStatusSelected = {},
            onPrioritySelected = {},
            onAssigneeSelected = {},
            onDueDateSelected = {},
            onApplyClick = {},
            onClearClick = {},
            onBack = {})
    }
}