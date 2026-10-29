package com.mentorship.compose.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentorship.compose.domain.entity.TaskFilter
import com.mentorship.compose.domain.usecase.GetTasksListUseCase
import com.mentorship.compose.domain.usecase.GetUsersUseCase
import com.mentorship.compose.tasks.filters.DueDate
import com.mentorship.compose.ui.components.Priority
import com.mentorship.compose.ui.components.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TasksListViewModel @Inject constructor(
    getTasksList: GetTasksListUseCase,
    getUsers: GetUsersUseCase,
) : ViewModel() {

    private val _appliedFilters = MutableStateFlow(TaskFilter.none())
    val appliedFilters = _appliedFilters.asStateFlow()

    private val _draftFilters = MutableStateFlow(_appliedFilters.value)
    val draftFilters = _draftFilters.asStateFlow()

    val state = combine(getTasksList(), appliedFilters) { tasks, filters ->
        tasks.filter { filters.matches(it) }
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5_000L),
        initialValue = emptyList()
    )

    val users = getUsers().stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5_000L),
        initialValue = emptyList()
    )

    val filteredTasksCount = combine(getTasksList(), draftFilters) { tasks, filters ->
        tasks.count { filters.matches(it) }
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5_000L),
        initialValue = 0,
    )

    fun setDraftFilters() {
        _draftFilters.value = _appliedFilters.value
    }

    fun onStatusSelected(status: Status) {
        _draftFilters.update { it.copy(statuses = it.statuses.toggle(status)) }
    }

    fun onPrioritySelected(priority: Priority) {
        _draftFilters.update { it.copy(priorities = it.priorities.toggle(priority)) }
    }

    fun onAssigneeSelected(id: String) {
        _draftFilters.update { it.copy(assigneeId = id) }
    }

    fun onDueDateSelected(dueDate: DueDate) {
        _draftFilters.update { it.copy(dueFilter = dueDate) }
    }

    fun onClearClick() {
        _draftFilters.value = TaskFilter.none()
    }

    fun onApplyClick() {
        _appliedFilters.value = _draftFilters.value
    }

    private fun <T> Set<T>.toggle(item: T): Set<T> = if (item in this) this - item else this + item
}