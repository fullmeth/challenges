package com.mentorship.compose.data.mock

import com.mentorship.compose.data.entity.ChatDto
import com.mentorship.compose.data.entity.ChecklistItem
import com.mentorship.compose.data.entity.TaskDto
import com.mentorship.compose.data.entity.TaskPriority
import com.mentorship.compose.data.entity.TaskStatus
import com.mentorship.compose.data.entity.User
import java.time.LocalDateTime

object MockData {
    private val now: LocalDateTime = LocalDateTime.now()
    private fun inDays(n: Long): LocalDateTime = now.plusDays(n)

    val users = listOf(
        User("Alex", "Morgan", "Project Manager"),
        User("Emma", "Wilson", "Android Developer"),
        User("Daniel", "Brown", "QA Engineer"),
        User("Sophia", "Taylor", "Product Designer"),
        User("Oliver", "Davis", "Backend Developer"),
        User("Mia", "Anderson", "UX Designer"),
    )

    val chat = ChatDto(
        name = "Mobile Team",
        description = "Discussion about the mobile application.",
        lastActivity = inDays(-1),
        participantsIds = setOf(
            "AlexMorgan",
            "EmmaWilson",
            "OliverDavis",
            "DanielBrown",
            "MiaAnderson"
        ),
    )

    val tasks = listOf(
        TaskDto(
            id = 1,
            title = "Prepare monthly report",
            description = "Prepare and review the monthly operational report.",
            status = TaskStatus.InProgress,
            priority = TaskPriority.Medium,
            assigneeId = "AlexMorgan",
            dueDate = inDays(2),
            participantsIds = setOf("AlexMorgan", "EmmaWilson", "DanielBrown", "SophiaTaylor"),
            checklist = listOf(
                ChecklistItem(1, "Collect department data", true),
                ChecklistItem(2, "Draft summary", true),
                ChecklistItem(3, "Review with finance", false),
                ChecklistItem(4, "Send for approval", false),
            ),
        ),
        TaskDto(
            id = 2,
            title = "Update onboarding flow",
            description = "Redesign the first-run onboarding screens for new hires.",
            status = TaskStatus.ToDo,
            priority = TaskPriority.Low,
            assigneeId = "SophiaTaylor",
            dueDate = inDays(9),
            participantsIds = setOf("SophiaTaylor", "MiaAnderson"),
            checklist = listOf(
                ChecklistItem(1, "Draft new flow", false),
                ChecklistItem(2, "Review with product", false),
            ),
        ),
        TaskDto(
            id = 3,
            title = "Fix authentication bug",
            description = "Users occasionally get logged out during token refresh.",
            status = TaskStatus.Done,
            priority = TaskPriority.High,
            assigneeId = "EmmaWilson",
            dueDate = inDays(-6),
            participantsIds = setOf("EmmaWilson", "OliverDavis", "DanielBrown"),
            checklist = listOf(
                ChecklistItem(1, "Reproduce issue", true),
                ChecklistItem(2, "Patch token interceptor", true),
                ChecklistItem(3, "Regression tests", true),
            ),
            completedById = "EmmaWilson",
            completedDate = inDays(-7),
            note = "Root cause was a race condition in the token refresh interceptor.",
        ),
        TaskDto(
            id = 4,
            title = "Review API integration",
            description = "Validate the new payments API integration end to end.",
            status = TaskStatus.InProgress,
            priority = TaskPriority.High,
            assigneeId = "OliverDavis",
            dueDate = inDays(-1),
            participantsIds = setOf("OliverDavis", "DanielBrown", "AlexMorgan"),
            checklist = listOf(
                ChecklistItem(1, "Verify auth flow", true),
                ChecklistItem(2, "Test edge cases", false),
                ChecklistItem(3, "Load testing", false),
            ),
        ),
        TaskDto(
            id = 5,
            title = "Prepare release notes",
            description = "Summarize changes for the v2.4 release.",
            status = TaskStatus.Done,
            priority = TaskPriority.Medium,
            assigneeId = "AlexMorgan",
            dueDate = inDays(12),
            participantsIds = setOf("AlexMorgan", "MiaAnderson"),
            checklist = listOf(ChecklistItem(1, "Collect changelog", false)),
        ),
        TaskDto(
            id = 6,
            title = "Implement user search",
            description = "Add search-by-name to the users directory.",
            status = TaskStatus.Done,
            priority = TaskPriority.Low,
            assigneeId = "MiaAnderson",
            dueDate = inDays(-11),
            participantsIds = setOf("MiaAnderson", "EmmaWilson"),
            checklist = listOf(
                ChecklistItem(1, "Design search UI", true),
                ChecklistItem(2, "Implement debounce", true),
                ChecklistItem(3, "QA pass", true),
            ),
            completedById = "MiaAnderson",
            completedDate = inDays(-12),
            note = "Shipped with debounce and empty-state handling.",
        ),
    )
}