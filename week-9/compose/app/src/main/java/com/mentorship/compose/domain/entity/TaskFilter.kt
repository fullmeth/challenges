package com.mentorship.compose.domain.entity

import com.mentorship.compose.tasks.filters.DueDate
import com.mentorship.compose.ui.components.Priority
import com.mentorship.compose.ui.components.Status
import java.time.LocalDateTime

data class TaskFilter(
    val statuses: Set<Status> = emptySet(),
    val priorities: Set<Priority> = emptySet(),
    val assigneeId: String? = null,
    val dueFilter: DueDate = DueDate.AnyTime
) {
    val isActive: Boolean get() = this != none()

    fun matches(task: Task): Boolean {
        val today = LocalDateTime.now()
        if (statuses.isNotEmpty() && task.status !in statuses) return false
        if (priorities.isNotEmpty() && task.priority !in priorities) return false
        if (!assigneeId.isNullOrBlank() && task.assignee.id != assigneeId) return false
        return when (dueFilter) {
            DueDate.AnyTime -> true
            DueDate.ThisWeek -> task.status != Status.Done
                    && !task.dueDate.isBefore(today)
                    && !task.dueDate.isAfter(today.plusDays(7))

            DueDate.Overdue -> task.status != Status.Done && task.dueDate.isAfter(today)
        }
    }

    companion object {
        fun none() = TaskFilter()
    }
}