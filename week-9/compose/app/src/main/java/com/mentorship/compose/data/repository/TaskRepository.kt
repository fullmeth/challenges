package com.mentorship.compose.data.repository

import com.mentorship.compose.data.entity.TaskDto
import com.mentorship.compose.data.entity.TaskStatus
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    val tasks: Flow<List<TaskDto>>
    fun observeTask(id: Int): Flow<TaskDto?>
    fun updateStatus(id: Int, status: TaskStatus)
    fun toggleChecklistItem(taskId: Int, checklistItemId: Int)
}