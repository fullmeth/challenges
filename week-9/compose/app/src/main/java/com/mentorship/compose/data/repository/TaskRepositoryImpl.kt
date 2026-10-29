package com.mentorship.compose.data.repository

import com.mentorship.compose.data.entity.TaskDto
import com.mentorship.compose.data.entity.TaskStatus
import com.mentorship.compose.data.mock.MockData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor() : TaskRepository {
    private val _tasks = MutableStateFlow(MockData.tasks)
    override val tasks = _tasks.asStateFlow()

    override fun observeTask(id: Int): Flow<TaskDto?> = _tasks
        .map { tasks -> tasks.firstOrNull { it.id == id } }
        .distinctUntilChanged()

    override fun updateStatus(
        id: Int,
        status: TaskStatus
    ) = _tasks.update { tasks ->
        tasks.map { task ->
            if (task.id == id) {
                task.copy(
                    status = status,
                    completedById = if (status == TaskStatus.Done) task.completedById
                        ?: task.assigneeId else task.completedById,
                    completedDate = if (status == TaskStatus.Done) task.completedDate
                        ?: LocalDateTime.now() else task.completedDate
                )
            } else task
        }
    }

    override fun toggleChecklistItem(taskId: Int, checklistItemId: Int) = _tasks.update { tasks ->
        tasks.map { task ->
            if (task.id == taskId) task.copy(
                checklist = task.checklist.map { check ->
                    if (check.id == checklistItemId) check.copy(
                        done = !check.done
                    ) else check
                }
            ) else task
        }

    }
}