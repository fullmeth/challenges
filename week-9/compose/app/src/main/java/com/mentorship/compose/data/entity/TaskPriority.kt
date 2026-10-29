package com.mentorship.compose.data.entity

import com.mentorship.compose.ui.components.Priority

enum class TaskPriority {
    Low, Medium, High
}

fun TaskPriority.toEntity() = when (this) {
    TaskPriority.Low -> Priority.Low
    TaskPriority.Medium -> Priority.Medium
    TaskPriority.High -> Priority.High
}