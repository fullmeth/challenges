package com.mentorship.compose.data.entity

import com.mentorship.compose.domain.entity.Chat
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class ChatDto(
    val name: String,
    val description: String,
    val lastActivity: LocalDateTime,
    val participantsIds: Set<String>,
)

fun ChatDto.toEntity(participants: List<User>) = Chat(
    name = name,
    description = description,
    lastActivity = lastActivity.relativeTo(LocalDateTime.now()),
    participants = participants
)

fun LocalDateTime.relativeTo(other: LocalDateTime = LocalDateTime.now()): String {
    return when (val hours = ChronoUnit.HOURS.between(this, other)) {
        0L -> "less than an hour ago"
        1L -> "1 hour ago"
        -1L -> "in 1 hour"
        else -> if (hours > 0) "$hours hours ago" else "in ${-hours} hours"
    }
}