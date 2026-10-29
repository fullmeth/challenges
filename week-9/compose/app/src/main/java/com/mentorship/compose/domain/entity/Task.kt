package com.mentorship.compose.domain.entity

import com.mentorship.compose.data.entity.ChecklistItem
import com.mentorship.compose.data.entity.User
import com.mentorship.compose.ui.components.Priority
import com.mentorship.compose.ui.components.Status
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val status: Status,
    val priority: Priority,
    val assignee: User,
    val dueDate: LocalDateTime,
    val participants: List<User>,
    val checklist: List<ChecklistItem>,
    val completedBy: User? = null,
    val completedDate: LocalDateTime? = null,
    val note: String? = null,
) {
    companion object {
        fun empty() = Task(
            id = -1,
            title = "",
            description = "",
            status = Status.ToDo,
            priority = Priority.Low,
            assignee = User.empty(),
            dueDate = LocalDateTime.now(),
            participants = emptyList(),
            checklist = emptyList(),
            completedBy = null,
            completedDate = LocalDateTime.now(),
            note = null,
        )

        fun mock(status: Status = Status.InProgress) = Task(
            id = 1,
            title = "Prepare monthly report",
            description = "Prepare and review the monthly operational report.",
            status = status,
            priority = Priority.Medium,
            assignee = User("Alex", "Morgan", "Cowboy"),
            dueDate = LocalDateTime.now(),
            participants = listOf(
                User("John", "Travolta", "Powerful man")
            ),
            checklist = listOf(
                ChecklistItem(1, "Collect department data", true),
                ChecklistItem(2, "Draft summary", true),
                ChecklistItem(3, "Review with finance", false),
                ChecklistItem(4, "Send for approval", false),
            ),
        )
    }
}

fun LocalDateTime.formatDate(): String =
    format(DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault()))