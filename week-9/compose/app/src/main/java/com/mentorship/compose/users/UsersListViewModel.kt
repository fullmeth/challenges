package com.mentorship.compose.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentorship.compose.domain.usecase.GetChatUsersUseCase
import com.mentorship.compose.domain.usecase.GetTaskUsersUseCase
import com.mentorship.compose.navigation.UsersListRoute
import com.mentorship.compose.navigation.UsersSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn

@HiltViewModel(assistedFactory = UsersListViewModel.Factory::class)
class UsersListViewModel @AssistedInject constructor(
    @Assisted private val navKey: UsersListRoute,
    getChatUsers: GetChatUsersUseCase,
    getTaskUsers: GetTaskUsersUseCase,
) : ViewModel() {

    val state = when (navKey.source) {
        is UsersSource.Chat -> getChatUsers()
        is UsersSource.Task -> getTaskUsers(navKey.source.id)
    }.stateIn(viewModelScope, WhileSubscribed(5_000L), emptyList())

    @AssistedFactory
    interface Factory {
        fun create(navKey: UsersListRoute): UsersListViewModel
    }
}