@file:OptIn(ExperimentalMaterial3Api::class)

package com.mentorship.compose.tasks

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import com.mentorship.compose.data.entity.ChecklistItem
import com.mentorship.compose.data.entity.User
import com.mentorship.compose.domain.entity.Task
import com.mentorship.compose.domain.entity.formatDate
import com.mentorship.compose.ui.components.CustomCard
import com.mentorship.compose.ui.components.Priority
import com.mentorship.compose.ui.components.PriorityChip
import com.mentorship.compose.ui.components.Status
import com.mentorship.compose.ui.components.StatusChip
import com.mentorship.compose.ui.theme.Accent200
import com.mentorship.compose.ui.theme.Accent2_600
import com.mentorship.compose.ui.theme.ComposeTheme
import com.mentorship.compose.ui.theme.Divider
import com.mentorship.compose.ui.theme.PillShape

@Composable
fun TaskDetailsScreen(
    viewModel: TaskDetailsViewModel,
    onParticipantsClick: (Int) -> Unit,
    onChangeTaskStatus: (Int) -> Unit,
    onBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    TaskDetailsScreen(
        task = state,
        onParticipantsClick = onParticipantsClick,
        onStartTask = viewModel::onStartTask,
        onChangeTaskStatus = onChangeTaskStatus,
        onToggleChecklistItem = viewModel::changeChecklistStatus,
        onBack = onBack
    )
}

@Composable
private fun TaskDetailsScreen(
    task: Task,
    onParticipantsClick: (Int) -> Unit,
    onStartTask: () -> Unit,
    onChangeTaskStatus: (Int) -> Unit,
    onToggleChecklistItem: (Int) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Task Details",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
                .padding(12.dp),
            verticalArrangement = spacedBy(16.dp)
        ) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Row(horizontalArrangement = spacedBy(12.dp)) {
                StatusChip(status = task.status)
                PriorityChip(priority = Priority.High)
            }
            when (task.status) {
                Status.ToDo -> {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.labelMedium,
                    )
                    DetailsCard(
                        modifier = Modifier.fillMaxWidth(),
                        withHeadline = true,
                        assignee = task.assignee,
                        dueDate = task.dueDate.formatDate()
                    )
                }

                Status.Done -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            verticalArrangement = spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {

                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = Accent2_600,
                                modifier = Modifier.size(36.dp)
                            )
                            Text(
                                text = "Completed ${task.completedDate?.formatDate()}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                            Text(
                                text = "by ${task.completedBy?.displayName}",
                                style = MaterialTheme.typography.labelSmall,
                            )
                        }
                    }
                    task.note?.let {
                        CustomCard(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "COMPLETION NOTE",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                            )
                            Text(
                                text = task.note,
                                style = MaterialTheme.typography.labelMedium,
                            )
                        }
                    }
                }

                Status.InProgress -> Unit
            }
            when (task.status) {
                Status.ToDo -> {
                    AcceptanceCriteria(
                        modifier = Modifier.fillMaxWidth(),
                        checklist = task.checklist
                    )
                }

                Status.InProgress -> {
                    Progress(
                        modifier = Modifier.fillMaxWidth(),
                        checklist = task.checklist,
                        onToggleChecklistItem = onToggleChecklistItem,
                    )
                    DetailsCard(
                        modifier = Modifier.fillMaxWidth(),
                        withHeadline = false,
                        assignee = task.assignee,
                        dueDate = task.dueDate.formatDate()
                    )
                }

                Status.Done -> {
                    FinalChecklist(modifier = Modifier.fillMaxWidth(), checklist = task.checklist)
                }
            }
            CustomCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = dropUnlessResumed { onParticipantsClick(task.id) },
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(verticalArrangement = spacedBy(4.dp)) {
                        Text(
                            text = "PEOPLE INVOLVED",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = "${task.participants.size} paricipants",
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = spacedBy((-12).dp)
                    ) {
                        task.participants.take(3).forEach { user ->
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(PillShape)
                                    .background(Accent200)
                                    .border(2.dp, MaterialTheme.colorScheme.surface, PillShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = user.initials,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                        Spacer(Modifier.width(32.dp))
                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                            contentDescription = null
                        )
                    }
                }
            }

            if (task.status == Status.ToDo) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onStartTask
                ) {
                    Text(
                        text = "Start task",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White,
                    )
                }
            }

            ChangeStatusButton(
                modifier = Modifier.fillMaxWidth(),
                status = task.status,
                onClick = { onChangeTaskStatus(task.id) }
            )
        }
    }
}

@Composable
fun ChangeStatusButton(modifier: Modifier = Modifier, status: Status, onClick: () -> Unit) {
    val colors = if (status != Status.InProgress) {
        ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    } else ButtonDefaults.buttonColors()
    val text = when (status) {
        Status.ToDo -> "Change status"
        Status.InProgress -> "Update status"
        Status.Done -> "Reopen / change status"
    }

    Button(
        modifier = modifier,
        colors = colors,
        border = if (status != Status.InProgress) BorderStroke(1.dp, Divider) else null,
        onClick = onClick
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            color = if (status != Status.InProgress) MaterialTheme.colorScheme.onBackground else Color.White,
        )
    }
}

@Composable
fun TaskCheckboxRow(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .toggleable(
                value = checked,
                role = Role.Checkbox,
                onValueChange = onCheckedChange,
                enabled = enabled,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = if (checked) Icons.Filled.CheckCircle
            else Icons.Outlined.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (checked) Accent2_600 else Color.Unspecified,
            modifier = Modifier.size(12.dp)
        )

        Spacer(Modifier.width(12.dp))

        Text(
            text = text,
            textDecoration = if (checked) TextDecoration.LineThrough
            else TextDecoration.None,
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

@Composable
fun DetailsCard(
    modifier: Modifier = Modifier,
    withHeadline: Boolean,
    assignee: User,
    dueDate: String
) {
    CustomCard(modifier = modifier) {
        if (withHeadline) {
            Text(
                text = "DETAILS",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Assignee",
                style = MaterialTheme.typography.labelMedium,
            )
            Text(
                text = assignee.displayName,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Due date",
                style = MaterialTheme.typography.labelMedium,
            )
            Text(
                text = dueDate,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Composable
fun AcceptanceCriteria(modifier: Modifier = Modifier, checklist: List<ChecklistItem>) {
    Column(modifier = modifier, verticalArrangement = spacedBy(8.dp)) {
        Text(
            text = "ACCEPTANCE CRITERIA",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        checklist.forEach {
            TaskCheckboxRow(
                modifier = modifier.fillMaxWidth(),
                text = it.label,
                enabled = false,
                checked = false,
                onCheckedChange = { /* no-op */ }
            )
            HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = Divider)
        }
    }
}

@Composable
fun Progress(
    modifier: Modifier = Modifier,
    checklist: List<ChecklistItem>,
    onToggleChecklistItem: (Int) -> Unit
) {
    val totalTasks = checklist.size
    val readyTasks = checklist.count { it.done }
    CustomCard(modifier = modifier) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "PROGRESS",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = "$readyTasks of $totalTasks done",
                style = MaterialTheme.typography.labelMedium,
            )
        }
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            progress = { readyTasks / totalTasks.toFloat() },
            drawStopIndicator = {}
        )
        checklist.forEach { item ->
            TaskCheckboxRow(
                modifier = modifier.fillMaxWidth(),
                text = item.label,
                enabled = true,
                checked = item.done,
                onCheckedChange = { onToggleChecklistItem(item.id) }
            )
        }
    }
}

@Composable
fun FinalChecklist(modifier: Modifier = Modifier, checklist: List<ChecklistItem>) {
    Column(modifier = modifier, verticalArrangement = spacedBy(8.dp)) {
        Text(
            text = "FINAL CHECKLIST",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        checklist.forEach {
            TaskCheckboxRow(
                modifier = modifier.fillMaxWidth(),
                text = it.label,
                enabled = false,
                checked = true,
                onCheckedChange = { /* no-op */ }
            )
        }
    }
}

@Preview
@Composable
private fun TaskDetailsScreenToDoPreview() {
    ComposeTheme {
        TaskDetailsScreen(Task.mock(Status.ToDo), {}, {}, {}, { }, {})
    }
}

@Preview
@Composable
private fun TaskDetailsScreenInProgressPreview() {
    ComposeTheme {
        TaskDetailsScreen(Task.mock(Status.InProgress), {}, {}, {}, { }, {})
    }
}

@Preview
@Composable
private fun TaskDetailsScreenDonePreview() {
    ComposeTheme {
        TaskDetailsScreen(Task.mock(Status.Done), {}, {}, {}, { }, {})
    }
}