@file:OptIn(ExperimentalCoroutinesApi::class)

package com.mentorship.compose.domain.usecase

import com.mentorship.compose.data.entity.User
import com.mentorship.compose.data.repository.ChatRepository
import com.mentorship.compose.data.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetChatUsersUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository
) {

    operator fun invoke(): Flow<List<User>> = chatRepository.getChat()
        .map { it.participantsIds }
        .distinctUntilChanged()
        .flatMapLatest { userRepository.getByIds(it) }
}