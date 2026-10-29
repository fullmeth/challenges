package com.mentorship.compose.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentorship.compose.domain.entity.Chat
import com.mentorship.compose.domain.usecase.GetChatUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ChatDetailsViewModel @Inject constructor(getChat: GetChatUseCase) : ViewModel() {

    val state = getChat().stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5_000L),
        initialValue = Chat.empty()
    )
}