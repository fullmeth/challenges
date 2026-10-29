@file:OptIn(ExperimentalCoroutinesApi::class)

package com.mentorship.compose.domain.usecase

import com.mentorship.compose.data.repository.TaskRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class ToggleChecklistStatusUseCase @Inject constructor(private val taskRepository: TaskRepository) {

    operator fun invoke(taskId: Int, checklistItemId: Int) {
        taskRepository.toggleChecklistItem(taskId, checklistItemId)
    }
}