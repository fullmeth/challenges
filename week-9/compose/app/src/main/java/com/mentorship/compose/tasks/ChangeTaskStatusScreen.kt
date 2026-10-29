@file:OptIn(ExperimentalMaterial3Api::class)

package com.mentorship.compose.tasks

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import com.mentorship.compose.domain.entity.Task
import com.mentorship.compose.ui.components.CustomCard
import com.mentorship.compose.ui.components.Status
import com.mentorship.compose.ui.theme.Accent100
import com.mentorship.compose.ui.theme.ComposeTheme
import com.mentorship.compose.ui.theme.Divider

@Composable
fun ChangeTaskStatusScreen(
    viewModel: ChangeTaskStatusViewModel,
    onBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val selectedStatus by viewModel.selectedStatus.collectAsStateWithLifecycle()
    ChangeTaskStatusScreen(
        task = state,
        onSave = dropUnlessResumed {
            viewModel.onChangeTaskStatus()
            onBack()
        },
        onBack = onBack,
        selected = selectedStatus ?: state.status,
        onSelectNewStatus = viewModel::onSelectNewStatus
    )
}

@Composable
fun ChangeTaskStatusScreen(
    task: Task,
    selected: Status,
    onSelectNewStatus: (Status) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
) {
    val options = Status.entries
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Change Task Status",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = dropUnlessResumed { onBack() }) {
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
            CustomCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "TASK",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    text = "Current status: ${task.status.text}",
                    style = MaterialTheme.typography.labelMedium,
                )
            }

            RadioOptions(
                options = options,
                selected = selected,
                onOptionSelected = onSelectNewStatus
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onSave
            ) {
                Text(
                    text = "Save",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
fun RadioOptions(options: List<Status>, selected: Status, onOptionSelected: (Status) -> Unit) {
    Column(Modifier.selectableGroup(), verticalArrangement = spacedBy(8.dp)) {
        options.forEach { option ->
            val description = when (option) {
                Status.ToDo -> "Not started yet."
                Status.InProgress -> "Work is underway."
                Status.Done -> "Work has been completed."
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(
                    1.dp,
                    if (selected == option) MaterialTheme.colorScheme.primary else Divider
                ),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = if (selected == option) Accent100 else Color.Transparent)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 4.dp)
                        .selectable(
                            selected = selected == option,
                            role = Role.RadioButton,
                            onClick = { onOptionSelected(option) }
                        )
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selected == option,
                        onClick = null
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(verticalArrangement = spacedBy(4.dp)) {
                        Text(
                            text = option.text,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                        Text(
                            text = description,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun ChangTaskStatusScreenPreview() {
    ComposeTheme {
        ChangeTaskStatusScreen(Task.mock(), Status.InProgress, {}, { }, {})
    }
}