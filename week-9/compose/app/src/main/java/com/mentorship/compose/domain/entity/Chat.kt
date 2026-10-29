package com.mentorship.compose.domain.entity

import com.mentorship.compose.data.entity.User

data class Chat(
    val name: String,
    val description: String,
    val lastActivity: String,
    val participants: List<User>,
) {
    companion object {
        fun empty() = Chat(
            name = "",
            description = "",
            lastActivity = "",
            participants = emptyList()
        )

        fun mock() = Chat(
            name = "Mobile Team",
            description = "Discussion about the mobile application.",
            lastActivity = "24 hours ago",
            participants = listOf(
                User("John", "Travolta", "Powerful man")
            )
        )
    }
}