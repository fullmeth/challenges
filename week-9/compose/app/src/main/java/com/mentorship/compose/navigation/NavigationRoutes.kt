package com.mentorship.compose.navigation

import androidx.navigation3.runtime.NavKey

data object StartRoute : NavKey
data object ChatDetailsRoute : NavKey
data class UsersListRoute(val source: UsersSource) : NavKey
data object TasksListRoute : NavKey
data class TaskRoute(val taskId: Int) : NavKey
data object FilterRoute : NavKey
data class ChangeTaskStatusRoute(val taskId: Int) : NavKey

sealed interface UsersSource {
    data class Task(val id: Int) : UsersSource
    data class Chat(val id: Int) : UsersSource
}