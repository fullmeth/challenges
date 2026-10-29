@file:OptIn(ExperimentalCoroutinesApi::class)

package com.mentorship.compose.domain.usecase

import com.mentorship.compose.data.entity.toEntity
import com.mentorship.compose.data.repository.ChatRepository
import com.mentorship.compose.data.repository.UserRepository
import com.mentorship.compose.domain.entity.Chat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository
) {

    operator fun invoke(): Flow<Chat> = chatRepository.getChat()
        .flatMapLatest { chat ->
            userRepository.getByIds(chat.participantsIds).map { users ->
                chat.toEntity(users)
            }
        }
}