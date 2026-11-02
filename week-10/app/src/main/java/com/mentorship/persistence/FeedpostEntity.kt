package com.mentorship.persistence

import androidx.room3.Embedded
import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.Junction
import androidx.room3.PrimaryKey
import androidx.room3.Relation

@Entity
data class FeedPostEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val description: String,
)

@Entity(primaryKeys = ["feedPostId", "mediaFileId"], indices = [Index("feedPostId", "mediaFileId")])
data class FeedPostMediaFileCrossRef(
    val feedPostId: Long,
    val mediaFileId: Long,
)

data class FeedPostWithMediaFiles(
    @Embedded val feedPostEntity: FeedPostEntity,
    @Relation(
        parentColumns = ["id"],
        entityColumns = ["id"],
        associateBy = Junction(
            value = FeedPostMediaFileCrossRef::class,
            parentColumns = ["feedPostId"],
            entityColumns = ["mediaFileId"]
        )
    )
    val mediaFile: List<MediaFileEntity>
)