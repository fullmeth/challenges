package com.mentorship.compose.data.repository

import com.mentorship.compose.data.entity.ChatDto
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChat(): Flow<ChatDto>
}