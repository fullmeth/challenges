package com.mentorship.compose.data.entity

import com.mentorship.compose.domain.entity.Task
import java.time.LocalDateTime

data class TaskDto(
    val id: Int,
    val title: String,
    val description: String,
    val status: TaskStatus,
    val priority: TaskPriority,
    val assigneeId: String,
    val dueDate: LocalDateTime,
    val participantsIds: Set<String>,
    val checklist: List<ChecklistItem>,
    val completedById: String? = null,
    val completedDate: LocalDateTime? = null,
    val note: String? = null,
)

fun TaskDto.toEntity(assignee: User, participants: List<User>, completedBy: User?) = Task(
    id = id,
    title = title,
    description = description,
    status = status.toEntity(),
    priority = priority.toEntity(),
    assignee = assignee,
    dueDate = dueDate,
    participants = participants,
    checklist = checklist,
    completedBy = completedBy,
    completedDate = completedDate,
    note = note,
)