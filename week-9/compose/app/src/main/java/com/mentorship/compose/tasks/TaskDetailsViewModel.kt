package com.mentorship.compose.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentorship.compose.domain.entity.Task
import com.mentorship.compose.domain.usecase.ChangeTaskStatusUseCase
import com.mentorship.compose.domain.usecase.GetTaskDetailsUseCase
import com.mentorship.compose.domain.usecase.ToggleChecklistStatusUseCase
import com.mentorship.compose.navigation.TaskRoute
import com.mentorship.compose.ui.components.Status
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn

@HiltViewModel(assistedFactory = TaskDetailsViewModel.Factory::class)
class TaskDetailsViewModel @AssistedInject constructor(
    @Assisted private val navKey: TaskRoute,
    getTaskDetails: GetTaskDetailsUseCase,
    private val changeTaskStatus: ChangeTaskStatusUseCase,
    private val toggleChecklistStatus: ToggleChecklistStatusUseCase
) : ViewModel() {

    val state = getTaskDetails(navKey.taskId).stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5_000L),
        initialValue = Task.empty()
    )

    fun onStartTask() = changeTaskStatus(navKey.taskId, Status.InProgress)

    fun changeChecklistStatus(id: Int) = toggleChecklistStatus(navKey.taskId, id)

    @AssistedFactory
    interface Factory {
        fun create(navKey: TaskRoute): TaskDetailsViewModel
    }
}