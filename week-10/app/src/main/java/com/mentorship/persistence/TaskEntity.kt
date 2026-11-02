package com.mentorship.persistence

import androidx.room3.Embedded
import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.Junction
import androidx.room3.PrimaryKey
import androidx.room3.Relation
import java.time.LocalDateTime

@Entity
data class TaskEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val deadline: LocalDateTime,
    val createdById: Int,
)

@Entity(primaryKeys = ["taskId", "mediaFileId"], indices = [Index("taskId", "mediaFileId")])
data class TaskMediaFileCrossRef(
    val taskId: Long,
    val mediaFileId: Long,
)

data class TaskWithMediaFiles(
    @Embedded val task: TaskEntity,
    @Relation(
        parentColumns = ["id"],
        entityColumns = ["id"],
        associateBy = Junction(
            value = TaskMediaFileCrossRef::class,
            parentColumns = ["taskId"],
            entityColumns = ["mediaFileId"]
        )
    )
    val mediaFile: List<MediaFileEntity>
)