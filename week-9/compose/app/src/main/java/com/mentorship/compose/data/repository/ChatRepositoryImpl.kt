package com.mentorship.compose.data.repository

import com.mentorship.compose.data.mock.MockData
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor() : ChatRepository {

    override fun getChat() = flowOf(MockData.chat)
}