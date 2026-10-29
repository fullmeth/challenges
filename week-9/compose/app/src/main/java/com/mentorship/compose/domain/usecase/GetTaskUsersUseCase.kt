@file:OptIn(ExperimentalCoroutinesApi::class)

package com.mentorship.compose.domain.usecase

import com.mentorship.compose.data.entity.User
import com.mentorship.compose.data.repository.TaskRepository
import com.mentorship.compose.data.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class GetTaskUsersUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository
) {

    operator fun invoke(taskId: Int): Flow<List<User>> = taskRepository.observeTask(taskId)
        .mapNotNull { it?.participantsIds }
        .distinctUntilChanged()
        .flatMapLatest { userRepository.getByIds(it) }
}