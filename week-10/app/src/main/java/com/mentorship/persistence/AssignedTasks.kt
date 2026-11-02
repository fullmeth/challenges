package com.mentorship.persistence

import androidx.room3.Embedded
import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.Junction
import androidx.room3.Relation

@Entity(primaryKeys = ["taskId", "userId"], indices = [Index("taskId", "userId")])
data class AssignedTasksCrossRef(
    val taskId: Long,
    val userId: Long,
)

data class TaskWithAssignedUsers(
    @Embedded val task: TaskEntity,
    @Relation(
        parentColumns = ["id"],
        entityColumns = ["id"],
        associateBy = Junction(
            value = AssignedTasksCrossRef::class,
            parentColumns = ["taskId"],
            entityColumns = ["userId"],
        )
    )
    val user: List<UserEntity>
)

data class UserWithAssignedTasks(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumns = ["id"],
        entityColumns = ["id"],
        associateBy = Junction(
            value = AssignedTasksCrossRef::class,
            parentColumns = ["userId"],
            entityColumns = ["taskId"],
        )
    )
    val task: List<TaskEntity>
)