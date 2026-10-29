package com.mentorship.compose.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentorship.compose.domain.entity.Task
import com.mentorship.compose.domain.usecase.ChangeTaskStatusUseCase
import com.mentorship.compose.domain.usecase.GetTaskDetailsUseCase
import com.mentorship.compose.navigation.ChangeTaskStatusRoute
import com.mentorship.compose.ui.components.Status
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel(assistedFactory = ChangeTaskStatusViewModel.Factory::class)
class ChangeTaskStatusViewModel @AssistedInject constructor(
    @Assisted private val navKey: ChangeTaskStatusRoute,
    getTaskDetails: GetTaskDetailsUseCase,
    private val changeTaskStatus: ChangeTaskStatusUseCase,
) : ViewModel() {

    val state = getTaskDetails(navKey.taskId).stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5_000L),
        initialValue = Task.empty()
    )

    private val _selectedStatus = MutableStateFlow<Status?>(null)
    val selectedStatus = _selectedStatus.asStateFlow()

    fun onChangeTaskStatus() {
        val status = _selectedStatus.value ?: state.value.status
        changeTaskStatus(navKey.taskId, status)
    }

    fun onSelectNewStatus(status: Status) {
        _selectedStatus.value = status
    }

    @AssistedFactory
    interface Factory {
        fun create(navKey: ChangeTaskStatusRoute): ChangeTaskStatusViewModel
    }
}