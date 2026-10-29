@file:OptIn(ExperimentalCoroutinesApi::class)

package com.mentorship.compose.domain.usecase

import com.mentorship.compose.data.entity.toEntity
import com.mentorship.compose.data.repository.TaskRepository
import com.mentorship.compose.data.repository.UserRepository
import com.mentorship.compose.domain.entity.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTaskDetailsUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository
) {

    operator fun invoke(taskId: Int): Flow<Task> = taskRepository.observeTask(taskId)
        .filterNotNull()
        .flatMapLatest { task ->
            val userIds = buildSet {
                add(task.assigneeId)
                addAll(task.participantsIds)
                task.completedById?.let { add(it) }
            }

            userRepository.getByIds(userIds).map { users ->
                val usersById = users.associateBy { it.id }

                task.toEntity(
                    assignee = usersById.getValue(task.assigneeId),
                    participants = task.participantsIds.map { usersById.getValue(it) },
                    completedBy = task.completedById?.let { usersById.getValue(it) }
                )
            }
        }
}