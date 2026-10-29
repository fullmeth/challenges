package com.mentorship.compose.data.entity

import com.mentorship.compose.ui.components.Status

enum class TaskStatus {
    ToDo, InProgress, Done
}

fun TaskStatus.toEntity() = when (this) {
    TaskStatus.ToDo -> Status.ToDo
    TaskStatus.InProgress -> Status.InProgress
    TaskStatus.Done -> Status.Done
}