package com.mentorship.persistence

import androidx.room3.Embedded
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import androidx.room3.Relation

@Entity
data class UserEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val occupation: String,
)

data class UserWithCreatedTasks(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumns = ["id"],
        entityColumns = ["createdById"]
    )
    val tasks: List<TaskEntity>
)