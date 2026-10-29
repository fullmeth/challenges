@file:OptIn(ExperimentalCoroutinesApi::class)

package com.mentorship.compose.domain.usecase

import com.mentorship.compose.data.entity.TaskStatus
import com.mentorship.compose.data.repository.TaskRepository
import com.mentorship.compose.ui.components.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class ChangeTaskStatusUseCase @Inject constructor(private val taskRepository: TaskRepository) {

    operator fun invoke(taskId: Int, status: Status) {
        val status = when (status) {
            Status.ToDo -> TaskStatus.ToDo
            Status.InProgress -> TaskStatus.InProgress
            Status.Done -> TaskStatus.Done
        }
        taskRepository.updateStatus(taskId, status)
    }
}